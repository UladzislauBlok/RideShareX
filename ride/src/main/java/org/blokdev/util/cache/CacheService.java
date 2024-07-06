package org.blokdev.util.cache;

import java.util.Optional;
import java.util.UUID;

public interface CacheService {
    void putUserId(String email, UUID userId);
    Optional<UUID> getUserIdByEmail(String email);
    void putUserRating(String rating);
    boolean isUserRatingPresent(String rating);
    void invalidateUserRating(String rating);
}
