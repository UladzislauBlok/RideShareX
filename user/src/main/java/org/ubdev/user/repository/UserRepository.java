package org.ubdev.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ubdev.user.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Modifying
    @Query(value = "UPDATE User u SET u.email = :newEmail WHERE u.email = :oldEmail")
    void updateEmail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail);

    void deleteByEmail(String email);

    @Query(value = "SELECT u.id FROM User u WHERE u.email = :email")
    UUID findIdByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);
}
