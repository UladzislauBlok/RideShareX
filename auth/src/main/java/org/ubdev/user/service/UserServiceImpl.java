package org.ubdev.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.CreateUserMicroserviceRequestDto;
import org.ubdev.user.exception.EmailAlreadyExistException;
import org.ubdev.user.mapper.UserMapper;
import org.ubdev.user.model.User;
import org.ubdev.user.repository.UserRepository;

import static org.ubdev.user.config.UserConstants.EMAIL_ALREADY_EXIST_MESSAGE;

@Transactional
@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void createUser(CreateUserDto dto, MultipartFile image) {
        User user = userMapper.mapCreateRequestToUser(dto);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistException(EMAIL_ALREADY_EXIST_MESSAGE.formatted(user.getEmail()));
        }

        userRepository.saveUser(user);
        String photoPath = user.getId().toString() + image.getOriginalFilename();
        CreateUserMicroserviceRequestDto requestDto = userMapper.mapCreateRequestToUserMicroserviceRequest(dto, photoPath);
    }
}
