package org.ubdev.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.CreateUserMicroserviceRequest;
import org.ubdev.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", expression = "java( UUID.randomUUID().toString() )"),
            @Mapping(target = "isEmailConfirmed", expression = "java(false)"),
    })
    User mapCreateRequestToUser(CreateUserDto createUserDto);

    @Mappings({
            @Mapping(target = "id", expression = "java( UUID.randomUUID().toString() )"),
    })
    CreateUserMicroserviceRequest mapCreateRequestToUserMicroserviceRequest(CreateUserDto createUserDto, String photoPath);
}
