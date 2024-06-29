package org.blokdev.controller;

import lombok.RequiredArgsConstructor;
import org.blokdev.dto.CityDto;
import org.blokdev.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ride")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping("/cities/{countyCode}")
    public ResponseEntity<List<CityDto>> getCitiesByCountyCode(@PathVariable String countyCode) {
        return ResponseEntity.ok(cityService.getCitiesByCountry(countyCode));
    }
}
