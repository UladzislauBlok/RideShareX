package org.blokdev.repository;

import org.blokdev.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {

    @Query("SELECT t FROM Trip t WHERE t.departureCity = :departureCity AND t.arrivalCity = :arrivalCity " +
           "AND t.departureTime >= :departureTime AND t.departureTime < :departureTimePlusOneDay")
    Page<Trip> findAllByRouteAndTime(@Param("departureCity")String departureCity,
                                     @Param("arrivalCity")String arrivalCity,
                                     @Param("departureTime")LocalDateTime departureTime,
                                     @Param("departureTimePlusOneDay")LocalDateTime departureTimePlusOneDay,
                                     Pageable pageable);
}
