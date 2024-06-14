package org.ubdev.rating.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record RatingDto (
        UUID id,

        @JsonProperty("rated_user_id")
        UUID ratedUserId,

        @JsonProperty("rated_user_name")
        String ratedUserName,

        @JsonProperty("rating_user_id")
        UUID ratingUserId,

        @JsonProperty("rated_user_name")
        String ratingUserName,

        @JsonProperty("rating_value")
        Integer ratingValue,

        @JsonProperty("rated_at")
        LocalDateTime ratedAt
) {}
