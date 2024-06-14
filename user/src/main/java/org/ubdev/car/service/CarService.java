package org.ubdev.car.service;

import org.ubdev.car.dto.CarCreateDto;
import org.ubdev.car.dto.CarDto;

import java.util.List;
import java.util.UUID;

public interface CarService {
    CarDto create(CarCreateDto dto, String email);
    List<CarDto> getAllByUserId(UUID id);
    List<CarDto> getAllByCurrentUser(String email);
    void deleteById(UUID id);
}
