package org.blokdev.controller;

import lombok.RequiredArgsConstructor;
import org.blokdev.dto.CountyDto;
import org.blokdev.service.CountyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ride")
@RequiredArgsConstructor
public class CountyController {
    private final CountyService countyService;

    @GetMapping("/countries")
    public ResponseEntity<List<CountyDto>> getCountries() {
        return ResponseEntity.ok(countyService.getCountries());
    }
}
