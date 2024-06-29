package org.blokdev.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "trip")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Trip {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "departure_city_code", nullable = false)
    private City departureCity;

    @ManyToOne
    @JoinColumn(name = "arrival_city_code", nullable = false)
    private City arrivalCity;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "fare", nullable = false)
    private Double fare;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @Column(name = "max_passenger_capacity", nullable = false)
    private Integer maxPassengerCapacity;

    @ElementCollection
    @CollectionTable(name = "trip_user", joinColumns = @JoinColumn(name = "trip_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "is_owner")
    private Map<UUID, Boolean> userIds;
}
