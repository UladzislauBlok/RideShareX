package org.blokdev.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Country {
    @Id
    @Column(length = 2)
    private String code;

    @Column(unique = true, length = 64, nullable = false)
    private String name;
}
