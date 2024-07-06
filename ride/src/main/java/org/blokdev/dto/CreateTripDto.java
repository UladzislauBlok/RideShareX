package org.blokdev.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTripDto (
        @Size(min = 3, max = 3)
        @JsonProperty("departure_city")
        String departureCity,

        @Size(min = 3, max = 3)
        @JsonProperty("arrival_city")
        String arrivalCity,

        @JsonProperty("departure_time")
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime departureTime,

        @JsonProperty("arrival_time")
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime arrivalTime,

        Double fare,

        @Max(8)
        @JsonProperty("max_passenger_capacity")
        Integer maxPassengerCapacity
)
{}