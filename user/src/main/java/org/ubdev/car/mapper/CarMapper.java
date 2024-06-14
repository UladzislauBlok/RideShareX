package org.ubdev.car.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.ubdev.car.dto.CarCreateDto;
import org.ubdev.car.model.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = "user", ignore = true)
    })
    Car mapCreateCarDtoToCar(CarCreateDto dto);


}
