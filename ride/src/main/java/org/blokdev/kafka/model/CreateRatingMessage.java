package org.blokdev.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;


public record CreateRatingMessage(
        @JsonProperty("rated_user")
        UUID ratedUser,

        @JsonProperty("rating_user")
        UUID ratingUser,

        @JsonProperty("rating_value")
        Integer ratingValue,

        @JsonProperty("rated_at")
        LocalDateTime ratedAt
) {}