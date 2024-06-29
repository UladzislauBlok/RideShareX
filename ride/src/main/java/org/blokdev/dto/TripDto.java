package org.blokdev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record TripDto (
        UUID id,

        @JsonProperty("departure_city")
        String departureCity,

        @JsonProperty("arrival_city")
        String arrivalCity,

        @JsonProperty("departure_time")
        LocalDateTime departureTime,

        @JsonProperty("arrival_time")
        LocalDateTime arrivalTime,

        Double fare,

        @JsonProperty("max_passenger_capacity")
        Integer maxPassengerCapacity,

        @JsonProperty("user_ids")
        Map<UUID, Boolean> userIds
)
{}