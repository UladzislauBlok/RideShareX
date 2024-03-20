package org.ubdev.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
    User createNewUserWithoutId(CreateUserDto dto);
    void updateUserFromUserDto(UserUpdateDto userDto, @MappingTarget User user);
}
