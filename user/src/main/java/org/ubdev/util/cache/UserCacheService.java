package org.ubdev.util.cache;

import org.ubdev.user.model.User;

import java.util.UUID;

public interface UserCacheService {
    User getUserById(UUID id);
    void invalidateUserById(UUID id);
}
