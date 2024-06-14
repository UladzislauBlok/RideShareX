package org.ubdev.car.repository;

import org.ubdev.car.dto.CarDto;
import org.ubdev.car.model.Car;

import java.util.List;

public interface CarByUserRepository {
    CarDto saveDocumentWithUserEmail(Car car, String userEmail);
    List<CarDto> findAllDocumentsByUserEmail(String userEmail);
}
