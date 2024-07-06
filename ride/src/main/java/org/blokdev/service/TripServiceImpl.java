package org.blokdev.service;

import lombok.RequiredArgsConstructor;
import org.blokdev.dto.CreateTripDto;
import org.blokdev.dto.TripDto;
import org.blokdev.exception.MaxPassengerCapacityReachedException;
import org.blokdev.exception.TripNotFoundException;
import org.blokdev.http.client.UserFeignClient;
import org.blokdev.kafka.model.JoinTripMessage;
import org.blokdev.kafka.producer.TripMessageProducer;
import org.blokdev.mapper.TripMapper;
import org.blokdev.model.JoinTripAttempt;
import org.blokdev.model.JoinTripRequestData;
import org.blokdev.model.JoinTripStatus;
import org.blokdev.model.Trip;
import org.blokdev.repository.JoinTripRepository;
import org.blokdev.repository.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripMapper tripMapper;
    private final TripRepository tripRepository;
    private final UserFeignClient userFeignClient;
    private final TripMessageProducer messageProducer;
    private final JoinTripRepository joinTripRepository;

    @Override
    public TripDto createTrip(CreateTripDto dto, String ownerEmail) {
        Trip trip = tripMapper.mapCreateTripDtoToTrip(dto);
        UUID ownerId = userFeignClient.getIdByEmail(ownerEmail);
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

        messageProducer.sendJoinTripMessage(message);
    }
}
