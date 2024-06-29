package org.blokdev.repository;

import org.blokdev.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountyRepository extends JpaRepository<Country, String> {
}
