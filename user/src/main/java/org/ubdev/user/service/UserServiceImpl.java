package org.ubdev.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.exception.exceptions.DuplicateEmailException;
import org.ubdev.user.exception.exceptions.UserNotFoundException;
import org.ubdev.user.mapper.UserMapper;
import org.ubdev.user.model.User;
import org.ubdev.user.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::userToUserDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDto getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::userToUserDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Page<UserDto> getAll(int pageNum, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNum, pageSize))
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto create(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.email()))
            throw new DuplicateEmailException();

        User newUser = userMapper.createNewUserWithoutId(dto);
        // TODO image save logic
        newUser = userRepository.save(newUser);
        return userMapper.userToUserDto(newUser);
    }

    @Override
    public void deleteById(UUID id) {
        if (userRepository.existsById(id))
            throw new UserNotFoundException();
        userRepository.deleteById(id);
    }

    @Override
    public UserDto update(UserUpdateDto dto) {
        User user = userRepository.findById(dto.id())
                .orElseThrow(UserNotFoundException::new);
        userMapper.updateUserFromUserDto(dto, user);
        // TODO update image logic
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }
}
