package org.blokdev.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record JoinTripMessage (
        @JsonProperty("accept_username")
        String attemptUsername,

        @JsonProperty("accept_user_id")
        UUID attemptUserId,

        @JsonProperty("accept_link_id")
        UUID attemptId,

        @JsonProperty("owner_email")
        String ownerEmail
) {
}
