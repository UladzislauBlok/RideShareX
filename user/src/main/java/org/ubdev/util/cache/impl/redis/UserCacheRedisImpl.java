package org.ubdev.util.cache.impl.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.ubdev.user.exception.exceptions.UserNotFoundException;
import org.ubdev.user.model.User;
import org.ubdev.user.repository.UserRepository;
import org.ubdev.util.cache.UserCacheService;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserCacheRedisImpl implements UserCacheService {

    @Value("${spring.data.redis.expiration-time:10}")
    private Integer expirationTime;

    private final RedisTemplate<UUID, User> redisTemplate;
    private final UserRepository userRepository;

    private void saveUserToCache(UUID id, User user) {
        BoundValueOperations<UUID, User> ops = redisTemplate.boundValueOps(id);
        ops.set(user);
        ops.expire(Duration.ofMinutes(expirationTime));
    }

    @Override
    public User getUserById(UUID id) {
        BoundValueOperations<UUID, User> ops = redisTemplate.boundValueOps(id);
        return Optional.ofNullable(ops.get())
                .orElseGet(() ->
                {
                    User user = userRepository.findById(id)
                        .orElseThrow(UserNotFoundException::new);
                    saveUserToCache(user.getId(), user);
                    return user;
                });
    }

    @Override
    public void invalidateUserById(UUID id) {
        redisTemplate.delete(id);
    }
}
