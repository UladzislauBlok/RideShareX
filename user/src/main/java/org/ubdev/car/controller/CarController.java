package org.ubdev.car.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ubdev.car.dto.CarCreateDto;
import org.ubdev.car.dto.CarDto;
import org.ubdev.car.service.CarService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarDto> create(@RequestBody CarCreateDto carCreateDto, Principal principal) {
        CarDto carDto = carService.create(carCreateDto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(carDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CarDto>> getCarsByUserId(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok(carService.getAllByUserId(userId));
    }

    @GetMapping()
    public ResponseEntity<List<CarDto>> getCarsByCurrentUser(Principal principal) {
        return ResponseEntity.ok(carService.getAllByCurrentUser(principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable("id") UUID id) {
        carService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
