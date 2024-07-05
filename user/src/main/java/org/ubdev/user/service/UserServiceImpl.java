package org.ubdev.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.ubdev.kafka.model.user.CreateUserMessage;
import org.ubdev.kafka.model.user.DeleteUserMessage;
import org.ubdev.kafka.model.user.UpdateEmailMessage;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.exception.exceptions.UserNotFoundException;
import org.ubdev.user.mapper.UserMapper;
import org.ubdev.user.model.User;
import org.ubdev.user.repository.UserRepository;
import org.ubdev.util.cache.UserCacheService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCacheService cacheService;

    @Override
    public UserDto getById(UUID id) {
        User user = cacheService.getUserById(id);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.userToUserDto(user);
    }

    @Override
    public Page<UserDto> getAll(int pageNum, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNum, pageSize))
                .map(userMapper::userToUserDto);
    }

    @Override
    public void create(CreateUserMessage message) {
        User user = userMapper.mapCreateUserMessageToUser(message);
        userRepository.save(user);
    }

    @Override
    public UserDto update(UserUpdateDto dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        userMapper.updateUserFromDto(dto, user);
        // TODO update image logic
        userRepository.save(user);
        cacheService.invalidateUserById(user.getId());
        return userMapper.userToUserDto(user);
    }

    @Override
    public void deleteByMessage(DeleteUserMessage message) {
        UUID id = userRepository.findIdByEmail(message.email());
        userRepository.deleteByEmail(message.email());
        cacheService.invalidateUserById(id);
    }

    @Override
    public void updateEmail(UpdateEmailMessage message) {
        UUID id = userRepository.findIdByEmail(message.oldEmail());
        userRepository.updateEmail(message.oldEmail(), message.newEmail());
        cacheService.invalidateUserById(id);
    }
}
