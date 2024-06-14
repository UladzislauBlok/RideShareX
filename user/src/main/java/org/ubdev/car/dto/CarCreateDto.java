package org.ubdev.car.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public record CarCreateDto(
        String brand,
        String model,
        Integer year,
        String color,

        @Size(min = 3, max = 12)
        @JsonProperty("registration_number")
        String registrationNumber
) {
}
