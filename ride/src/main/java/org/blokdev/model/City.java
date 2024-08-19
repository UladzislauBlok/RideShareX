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
    @Column(length = 3)
    private String code;

    @Column(length = 64, unique = true, nullable = false)
    private String name;

    @Column(length = 2, unique = true, nullable = false)
    private String country;
}
