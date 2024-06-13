package org.ubdev.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.ubdev.kafka.model.rating.CreateRatingMessage;
import org.ubdev.rating.dto.RatingDto;
import org.ubdev.rating.mapper.RatingMapper;
import org.ubdev.rating.model.Rating;
import org.ubdev.rating.repository.RatingRepository;
import org.ubdev.user.model.User;
import org.ubdev.util.cache.UserCacheService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final UserCacheService cacheService;
    private final RatingMapper ratingMapper;

    @Override
    public void createRating(CreateRatingMessage message) {
        User ratedUser = cacheService.getUserById(message.ratedUser());
        User ratingUser = cacheService.getUserById(message.ratingUser());
        Rating rating = ratingMapper.mapCreateRatingMessageToRating(message, ratedUser, ratingUser);

        ratingRepository.save(rating);
    }

    @Override
    public Page<RatingDto> getRatingsByUserId(int pageNum, int pageSize, UUID userId) {
        return ratingRepository.findAllByRatedUserId(userId, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<RatingDto> getCurrentUserRatings(int pageNum, int pageSize, String email) {
        return ratingRepository.findAllByRatedUserEmail(email, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public void deleteRatingById(UUID id) {
        ratingRepository.deleteById(id);
    }
}
