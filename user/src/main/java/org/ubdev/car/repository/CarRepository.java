package org.ubdev.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ubdev.car.dto.CarDto;
import org.ubdev.car.model.Car;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID>, CarByUserRepository {
    List<CarDto> findAllByUser_Id(UUID userId);
}
