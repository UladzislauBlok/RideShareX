package org.blokdev.service;

import org.blokdev.dto.CreateTripDto;
import org.blokdev.dto.JoinTripDecisionDto;
import org.blokdev.dto.TripDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TripService {
    TripDto createTrip(CreateTripDto dto, String ownerEmail);
    Page<TripDto> getTripsByRouteAndTime(String departureCity,String arrivalCity,
                                         LocalDateTime departureTime, int pageNum, int pageSize);
    void deleteTrip(UUID id);
    void requestJoinTrip(UUID tripId, String userEmail);
    void processJoinTripDecision(JoinTripDecisionDto dto, String currentUserEmail);
    void endTrip(UUID tripId, String currentUserEmail);
    void startTrip(UUID tripId, String currentUserEmail);
    void createRating(UUID ratedUserId, Integer ratingValue, String currentUserEmail);
}
