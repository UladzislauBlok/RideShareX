package org.blokdev.service;

import lombok.RequiredArgsConstructor;
import org.blokdev.dto.CreateTripDto;
import org.blokdev.dto.JoinTripDecisionDto;
import org.blokdev.dto.TripDto;
import org.blokdev.exception.AttemptNotFoundException;
import org.blokdev.exception.InvalidRatingAttemptException;
import org.blokdev.exception.MaxPassengerCapacityReachedException;
import org.blokdev.exception.SelfJoinNotAllowedException;
import org.blokdev.exception.TripNotFoundException;
import org.blokdev.http.client.UserFeignClient;
import org.blokdev.kafka.model.CreateRatingMessage;
import org.blokdev.kafka.model.JoinTripMessage;
import org.blokdev.kafka.producer.RatingMessageProducer;
import org.blokdev.kafka.producer.TripMessageProducer;
import org.blokdev.mapper.TripMapper;
import org.blokdev.model.JoinTripAttempt;
import org.blokdev.model.JoinTripRequestData;
import org.blokdev.model.JoinTripStatus;
import org.blokdev.model.Trip;
import org.blokdev.model.TripStatus;
import org.blokdev.repository.JoinTripRepository;
import org.blokdev.repository.TripRepository;
import org.blokdev.util.cache.CacheService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.blokdev.constant.TripServiceConstants.ACCESS_DENIED_TRIP_EXCEPTION_MASSAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripMapper tripMapper;
    private final TripRepository tripRepository;
    private final UserFeignClient userFeignClient;
    private final TripMessageProducer tripMessageProducer;
    private final RatingMessageProducer ratingMessageProducer;
    private final JoinTripRepository joinTripRepository;
    private final CacheService cacheService;

    @Override
    public TripDto createTrip(CreateTripDto dto, String currentUserEmail) {
        Trip trip = tripMapper.mapCreateTripDtoToTrip(dto);
        UUID ownerId = cacheService.getUserIdByEmail(currentUserEmail)
                .orElse(getUserIdAndSaveToCache(currentUserEmail));
        Map<UUID, Boolean> userIds = new HashMap<>();
        userIds.put(ownerId, true);
        trip.setUserIds(userIds);
        trip = tripRepository.save(trip);
        return tripMapper.mapToDtoTrip(trip);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TripDto> getTripsByRouteAndTime(String departureCity, String arrivalCity,
                                                LocalDateTime departureTime, int pageNum, int pageSize) {
        LocalDateTime departureTimePlusOneDay = departureTime.plusDays(1);

        List<TripDto> filteredTrips = tripRepository.findAllByRouteAndTime(departureCity, arrivalCity,
                departureTime, departureTimePlusOneDay, PageRequest.of(pageNum, pageSize))
                .filter(trip -> trip.getMaxPassengerCapacity() < trip.getUserIds().size())
                .map(tripMapper::mapToDtoTrip)
                .toList();

        return new PageImpl<>(filteredTrips, PageRequest.of(pageNum, pageSize), filteredTrips.size());
    }

    @Override
    public void deleteTrip(UUID id) {
        if (!tripRepository.existsById(id)) {
            throw new TripNotFoundException();
        }
        tripRepository.deleteById(id);
    }

    @Override
    public void requestJoinTrip(UUID tripId, String userEmail) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        if (trip.getMaxPassengerCapacity() == trip.getUserIds().size()) {
            throw new MaxPassengerCapacityReachedException();
        }
        UUID ownerId = trip.getUserIds()
                .entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        JoinTripRequestData data = userFeignClient.getDataForJoinTripRequest(userEmail, ownerId);

        if (ownerId.equals(data.userId()))
            throw new SelfJoinNotAllowedException();

        JoinTripAttempt joinTripAttempt = JoinTripAttempt.builder()
                .trip(trip)
                .status(JoinTripStatus.WAITING)
                .build();

        joinTripAttempt = joinTripRepository.save(joinTripAttempt);

        JoinTripMessage message = new JoinTripMessage(
                data.name().concat(data.surname()),
                data.userId(),
                joinTripAttempt.getId(),
                data.ownerEmail()
                );

        tripMessageProducer.sendJoinTripMessage(message);
    }

    @Override
    public void processJoinTripDecision(JoinTripDecisionDto dto, String currentUserEmail) {
        JoinTripAttempt attempt = joinTripRepository.findById(dto.attemptId())
                .orElseThrow(AttemptNotFoundException::new);

        UUID ownerId = cacheService.getUserIdByEmail(currentUserEmail)
                .orElse(getUserIdAndSaveToCache(currentUserEmail));
        Trip trip = attempt.getTrip();

        if (trip.getMaxPassengerCapacity() == trip.getUserIds().size())
            throw new MaxPassengerCapacityReachedException();

        checkIsUserTripOwner(trip, ownerId);

        if (dto.decision().name().equals(JoinTripStatus.APPROVED.name())) {
            trip.getUserIds()
                    .put(dto.usesId(), false);

            attempt.setStatus(JoinTripStatus.APPROVED);
        } else {
            attempt.setStatus(JoinTripStatus.REJECTED);
        }

        tripRepository.save(trip);
        joinTripRepository.save(attempt);
    }

    private void checkIsUserTripOwner(Trip trip, UUID ownerId) {
        UUID tripOwnerId = trip.getUserIds()
                .entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        if (!ownerId.equals(tripOwnerId))
            throw new AccessDeniedException(ACCESS_DENIED_TRIP_EXCEPTION_MASSAGE);
    }

    @Override
    public void endTrip(UUID tripId, String currentUserEmail) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        UUID ownerId = cacheService.getUserIdByEmail(currentUserEmail)
                .orElse(getUserIdAndSaveToCache(currentUserEmail));

        checkIsUserTripOwner(trip, ownerId);
        trip.setStatus(TripStatus.COMPLETED);
        tripRepository.save(trip);

        Set<UUID> userIds = trip.getUserIds().keySet();
        userIds.forEach(currentUserId -> userIds.stream()
                .filter(id -> !id.equals(currentUserId))
                .forEach(id -> cacheService.putUserRating("%s - %s".formatted(currentUserId, id))));
    }

    @Override
    public void startTrip(UUID tripId, String currentUserEmail) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        UUID ownerId = cacheService.getUserIdByEmail(currentUserEmail)
                .orElse(getUserIdAndSaveToCache(currentUserEmail));

        checkIsUserTripOwner(trip, ownerId);

        trip.setStatus(TripStatus.ONGOING);
        tripRepository.save(trip);
    }

    @Override
    public void createRating(UUID ratedUserId, Integer ratingValue, String currentUserEmail) {
        UUID currentUserId = cacheService.getUserIdByEmail(currentUserEmail)
                .orElse(getUserIdAndSaveToCache(currentUserEmail));

        String ratingKey = "%s - %s".formatted(currentUserId, ratedUserId);
        if (cacheService.isUserRatingPresent(ratingKey)) {
            cacheService.invalidateUserRating(ratingKey);
            CreateRatingMessage message = new CreateRatingMessage(ratedUserId, currentUserId, ratingValue, LocalDateTime.now());
            ratingMessageProducer.sendRatingMessage(message);
        } else {
            throw new InvalidRatingAttemptException();
        }
    }

    private UUID getUserIdAndSaveToCache(String ownerEmail) {
        UUID id = userFeignClient.getIdByEmail(ownerEmail);
        cacheService.putUserId(ownerEmail, id);
        return id;
    }
}
