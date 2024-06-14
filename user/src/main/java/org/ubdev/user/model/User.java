package org.ubdev.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.ubdev.car.model.Car;
import org.ubdev.document.model.Document;
import org.ubdev.rating.model.Rating;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString(exclude = {"documents", "cars", "ratings"})
@Data
@Builder
public class User {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "animal_preference")
    @Enumerated(value = EnumType.STRING)
    private Preference animalPreference;

    @Column(name = "conversation_preference")
    @Enumerated(value = EnumType.STRING)
    private Preference conversationPreference;

    @Column(name = "music_preference")
    @Enumerated(value = EnumType.STRING)
    private Preference musicPreference;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Document> documents;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Car> cars;

    @OneToMany(mappedBy = "ratedUser")
    @JsonIgnore
    private List<Rating> ratings;
}
