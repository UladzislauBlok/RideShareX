package org.blokdev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "city")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class City {
    @Id
    private String code;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country country;
}
