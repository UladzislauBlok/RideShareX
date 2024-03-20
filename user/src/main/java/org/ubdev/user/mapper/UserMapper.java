package org.ubdev.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);

//    @Mapping(target = "id", ignore = true)
//    User createNewUserWithoutId(CreateUserDto dto);
}
