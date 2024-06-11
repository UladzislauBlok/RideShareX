package org.ubdev.user.mapper;

import org.mapstruct.*;
import org.ubdev.kafka.model.user.CreateUserMessage;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);

    @Mappings({
            @Mapping(target = "documents", expression = "java(null)"),
            @Mapping(target = "cars", expression = "java(null)"),
            @Mapping(target = "ratings", expression = "java(null)"),
            @Mapping(target = "registrationDate", expression = "java(LocalDate.now())")
    })
    User mapCreateUserMessageToUser(CreateUserMessage message);

    @Mappings({
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "registrationDate", ignore = true),
            @Mapping(target = "photoPath", ignore = true),
            @Mapping(target = "documents", ignore = true),
            @Mapping(target = "cars", ignore = true),
            @Mapping(target = "ratings", ignore = true)
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateDto userDto, @MappingTarget User user);
}
