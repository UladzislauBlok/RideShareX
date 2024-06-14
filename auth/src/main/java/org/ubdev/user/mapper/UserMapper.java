package org.ubdev.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.kafka.model.CreateUserMessage;
import org.ubdev.user.model.User;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", expression = "java( UUID.randomUUID() )"),
            @Mapping(target = "emailConfirmed", expression = "java(false)"),
    })
    User mapCreateRequestToUser(CreateUserDto createUserDto);

    CreateUserMessage mapCreateRequestToCreateUserMessage(CreateUserDto createUserDto, String photoPath, UUID id);
}
