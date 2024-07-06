package org.blokdev.mapper;

import org.blokdev.dto.CreateTripDto;
import org.blokdev.dto.TripDto;
import org.blokdev.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TripMapper {
    @Mappings({
            @Mapping(target = "id", expression = "java(UUID.randomUUID())"),
            @Mapping(target = "status", expression = "java(TripStatus.PLANNED)"),
            @Mapping(target = "userIds", ignore = true)
    })
    Trip mapCreateTripDtoToTrip(CreateTripDto createTripDto);

    TripDto mapToDtoTrip(Trip trip);
}
