package org.blokdev.service;

import lombok.RequiredArgsConstructor;
import org.blokdev.dto.CityDto;
import org.blokdev.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    public List<CityDto> getCitiesByCountry(String countryCode) {
        return cityRepository.findByCountry_Code(countryCode)
                .stream()
                .map(city -> new CityDto(city.getCode(), city.getName()))
                .toList();
    }
}
