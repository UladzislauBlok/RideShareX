package org.blokdev.service;

import lombok.RequiredArgsConstructor;
import org.blokdev.dto.CreateTripDto;
import org.blokdev.dto.TripDto;
import org.blokdev.exception.TripNotFoundException;
import org.blokdev.mapper.TripMapper;
import org.blokdev.model.Trip;
import org.blokdev.repository.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripMapper tripMapper;
    private final TripRepository tripRepository;

    @Override
    public TripDto createTrip(CreateTripDto dto) {
        Trip trip = tripMapper.mapCreateTripDtoToTrip(dto);
        trip = tripRepository.save(trip);
        return tripMapper.mapToDtoTrip(trip);
    }

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
}
