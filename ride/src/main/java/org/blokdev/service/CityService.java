package org.blokdev.service;

import org.blokdev.dto.CityDto;

import java.util.List;

public interface CityService {
    List<CityDto> getCitiesByCountry(String countryCode);
}
