package org.blokdev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record JoinTripDecisionDto (
        @JsonProperty("attempt_id")
        UUID attemptId,
        JoinDecision decision,
        @JsonProperty("user_id")
        UUID usesId
) {
}
