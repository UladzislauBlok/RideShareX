package org.blokdev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record JoinTripRequestDataDto(
        @JsonProperty("user_id")
        UUID userId,
        String name,
        String surname,

        @JsonProperty("owner_email")
        String ownerEmail
)
{}
