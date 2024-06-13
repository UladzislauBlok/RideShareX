package org.ubdev.rating.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ubdev.rating.dto.RatingDto;
import org.ubdev.rating.model.Rating;

import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    Page<RatingDto> findAllByRatedUserId(UUID userId, Pageable pageable);
    Page<RatingDto> findAllByRatedUserEmail(String email, Pageable pageable);
}
