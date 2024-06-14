package org.ubdev.rating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ubdev.rating.dto.RatingDto;
import org.ubdev.rating.service.RatingService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ratings")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/{id}")
    public ResponseEntity<Page<RatingDto>> getRatingsByUserId(@PathVariable("id") UUID userId,
                                                              @RequestParam int page,
                                                              @RequestParam int size) {
        return ResponseEntity.ok(ratingService.getRatingsByUserId(page, size, userId));
    }

    @GetMapping()
    public ResponseEntity<Page<RatingDto>> getRatingsByUserEmail(@RequestParam int page,
                                                              @RequestParam int size,
                                                              Principal principal) {
        return ResponseEntity.ok(ratingService.getCurrentUserRatings(page, size, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable("id") UUID id) {
        ratingService.deleteRatingById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
