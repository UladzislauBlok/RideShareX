package org.ubdev.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);

    @Mappings({
            @Mapping(target = "id", expression = "java(null)"),
            @Mapping(target = "photoUrl", expression = "java(null)"),
            @Mapping(target = "documents", expression = "java(null)"),
            @Mapping(target = "cars", expression = "java(null)"),
            @Mapping(target = "ratings", expression = "java(null)")
    })
    User createNewUserWithoutId(CreateUserDto dto);

    @Mappings({
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "registrationDate", ignore = true),
            @Mapping(target = "photoUrl", ignore = true),
            @Mapping(target = "documents", ignore = true),
            @Mapping(target = "cars", ignore = true),
            @Mapping(target = "ratings", ignore = true)
    })
    void updateUserFromUserDto(UserUpdateDto userDto, @MappingTarget User user);
}
