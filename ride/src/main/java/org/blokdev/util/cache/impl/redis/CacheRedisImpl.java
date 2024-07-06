package org.blokdev.util.cache.impl.redis;

import lombok.RequiredArgsConstructor;
import org.blokdev.util.cache.CacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CacheRedisImpl implements CacheService {

    @Value("${spring.data.redis.id.expiration-time.min:10}")
    private Integer idExpirationTime;

    @Value("${spring.data.redis.rating.expiration-time.hours:6}")
    private Integer ratingExpirationTime;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void putUserId(String email, UUID userId) {
        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(email);
        ops.set(userId.toString());
        ops.expire(Duration.ofMinutes(idExpirationTime));
    }

    @Override
    public Optional<UUID> getUserIdByEmail(String email) {
        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(email);
        return Optional.ofNullable(ops.get())
                .map(UUID::fromString);
    }

    @Override
    public void putUserRating(String rating) {
        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(rating);
        ops.set("waiting");
        ops.expire(Duration.ofHours(ratingExpirationTime));
    }

    @Override
    public boolean isUserRatingPresent(String rating) {
        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(rating);
        return ops.get() != null;
    }

    @Override
    public void invalidateUserRating(String rating) {
        redisTemplate.delete(rating);
    }
}
