package org.ubdev.user.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.ubdev.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<UserDetails> getUserByEmail(String email);
    boolean existsByEmail(String email);
    void saveUser(User user);
    void updateEmail(String oldEmail, String newEmail);
    void updatePassword(String email, String password);
    void deleteUser(String email);
    void deleteUserById(UUID id);
}
