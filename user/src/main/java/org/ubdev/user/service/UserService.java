package org.ubdev.user.service;

import org.springframework.data.domain.Page;
import org.ubdev.kafka.model.CreateUserMessage;
import org.ubdev.kafka.model.DeleteUserMessage;
import org.ubdev.kafka.model.UpdateEmailMessage;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;

import java.util.UUID;

public interface UserService {
    UserDto getById(UUID id);
    UserDto getByEmail(String email);
    Page<UserDto> getAll(int pageNum, int pageSize);
    void create(CreateUserMessage message);
    void deleteById(UUID id);
    UserDto update(UserUpdateDto dto);
    void deleteByMessage(DeleteUserMessage message);
    void updateEmail(UpdateEmailMessage message);
}
