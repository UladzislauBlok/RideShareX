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
            @Mapping(target = "userIds", expression = "java(mapUserIds(createTripDto))")
    })
    Trip mapCreateTripDtoToTrip(CreateTripDto createTripDto);

    default Map<UUID, Boolean> mapUserIds(CreateTripDto createTripDto) {
        Map<UUID, Boolean> userIds = new HashMap<>();
        userIds.put(createTripDto.ownerId(), true);
        return userIds;
    }

    TripDto mapToDtoTrip(Trip trip);
}
