package org.ubdev.rating.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ubdev.user.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ratings")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rated_user_id", nullable = false)
    private User ratedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_user_id", nullable = false)
    private User ratingUser;

    @Column(name = "rating_value", nullable = false)
    private Integer ratingValue;

    @Column(name = "rated_at", nullable = false)
    private LocalDateTime ratedAt;
}
