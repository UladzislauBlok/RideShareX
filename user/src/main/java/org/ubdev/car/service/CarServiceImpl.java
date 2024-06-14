package org.ubdev.car.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ubdev.car.dto.CarCreateDto;
import org.ubdev.car.dto.CarDto;
import org.ubdev.car.mapper.CarMapper;
import org.ubdev.car.model.Car;
import org.ubdev.car.repository.CarRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto create(CarCreateDto dto, String email) {
        Car car = carMapper.mapCreateCarDtoToCar(dto);
        return carRepository.saveDocumentWithUserEmail(car, email);
    }

    @Override
    public List<CarDto> getAllByUserId(UUID id) {
        return carRepository.findAllByUser_Id(id);
    }

    @Override
    public List<CarDto> getAllByCurrentUser(String email) {
        return carRepository.findAllDocumentsByUserEmail(email);
    }

    @Override
    public void deleteById(UUID id) {
        carRepository.deleteById(id);
    }
}
