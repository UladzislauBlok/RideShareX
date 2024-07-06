package org.ubdev.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record JoinTripRequestData (
        @JsonProperty("user_id")
        UUID userId,
        String name,
        String surname,

        @JsonProperty("owner_email")
        String ownerEmail
) {}
