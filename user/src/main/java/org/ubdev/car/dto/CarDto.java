package org.ubdev.car.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;


public record CarDto(
        UUID id,
        String brand,
        String model,
        Integer year,
        String color,
        @JsonProperty("registration_number")
        String registrationNumber
) {
}
