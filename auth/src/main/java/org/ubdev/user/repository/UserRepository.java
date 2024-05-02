package org.ubdev.user.repository;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository {
    Optional<UserDetails> getUserByEmail(String email);
    boolean existsByEmail(String email);
}
