package org.blokdev.repository;

import org.blokdev.model.JoinTripAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JoinTripRepository extends JpaRepository<JoinTripAttempt, UUID> {
}
