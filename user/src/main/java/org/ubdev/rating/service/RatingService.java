package org.ubdev.rating.service;

import org.springframework.data.domain.Page;
import org.ubdev.kafka.model.rating.CreateRatingMessage;
import org.ubdev.rating.dto.RatingDto;

import java.util.UUID;

public interface RatingService {
    void createRating(CreateRatingMessage message);
    Page<RatingDto> getRatingsByUserId(int pageNum, int pageSize, UUID userId);
    Page<RatingDto> getCurrentUserRatings(int pageNum, int pageSize, String email);
    void deleteRatingById(UUID id);
}
