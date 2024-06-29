package org.blokdev.service;

import lombok.RequiredArgsConstructor;
import org.blokdev.dto.CountyDto;
import org.blokdev.repository.CountyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountyServiceImpl implements CountyService {
    private final CountyRepository countyRepository;

    @Override
    public List<CountyDto> getCountries() {
        return countyRepository.findAll()
                .stream()
                .map(country -> new CountyDto(country.getCode(), country.getName()))
                .toList();
    }
}
