package org.ubdev.user.service;

import org.springframework.data.domain.Page;
import org.ubdev.kafka.model.user.CreateUserMessage;
import org.ubdev.kafka.model.user.DeleteUserMessage;
import org.ubdev.kafka.model.user.UpdateEmailMessage;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;

import java.util.UUID;

public interface UserService {
    UserDto getById(UUID id);
    Page<UserDto> getAll(int pageNum, int pageSize);
    void create(CreateUserMessage message);
    void deleteById(UUID id);
    UserDto update(UserUpdateDto dto);
    void deleteByMessage(DeleteUserMessage message);
    void updateEmail(UpdateEmailMessage message);
}
