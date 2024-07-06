package org.ubdev.user.service;

import org.springframework.data.domain.Page;
import org.ubdev.kafka.model.user.CreateUserMessage;
import org.ubdev.kafka.model.user.DeleteUserMessage;
import org.ubdev.kafka.model.user.UpdateEmailMessage;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.model.JoinTripRequestData;

import java.util.UUID;

public interface UserService {
    UserDto getById(UUID id);
    UserDto getUserByEmail(String email);
    Page<UserDto> getAll(int pageNum, int pageSize);
    JoinTripRequestData getJoinTripRequestData(String email, UUID ownerId);
    void create(CreateUserMessage message);
    UserDto update(UserUpdateDto dto, String email);
    void deleteByMessage(DeleteUserMessage message);
    void updateEmail(UpdateEmailMessage message);
}
