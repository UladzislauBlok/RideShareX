package org.ubdev.user.service;

import org.springframework.data.domain.Page;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;

import java.util.UUID;

public interface UserService {
    UserDto getById(UUID id);
    UserDto getByEmail(String email);
    Page<UserDto> getAll();
    UserDto create(CreateUserDto dto);
    void deleteById(UUID id);
    UserDto update(UserUpdateDto dto);
}
