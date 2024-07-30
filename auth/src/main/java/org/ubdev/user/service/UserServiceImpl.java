package org.ubdev.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.kafka.model.CreateUserMessage;
import org.ubdev.kafka.model.DeleteUserMessage;
import org.ubdev.kafka.model.UpdateEmailMessage;
import org.ubdev.kafka.producer.KafkaUserMessageProducer;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UpdateEmailDto;
import org.ubdev.user.dto.UpdatePasswordDto;
import org.ubdev.user.exception.EmailAlreadyExistException;
import org.ubdev.user.mapper.UserMapper;
import org.ubdev.user.model.User;
import org.ubdev.user.repository.UserRepository;

import java.util.UUID;

import static org.ubdev.user.config.UserConstants.EMAIL_ALREADY_EXIST_MESSAGE;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final KafkaUserMessageProducer userMessageProducer;

    @Override
    public void createUser(CreateUserDto dto, MultipartFile image) {
        User user = userMapper.mapCreateRequestToUser(dto);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistException(EMAIL_ALREADY_EXIST_MESSAGE.formatted(user.getEmail()));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveUser(user);
        String photoPath = user.getId().toString() + image.getOriginalFilename();
        CreateUserMessage message = userMapper.mapCreateRequestToCreateUserMessage(dto, photoPath, user.getId());

        userMessageProducer.sendCreateUserMessage(message);
        //todo publish message for imageMicro and UserMicro
    }

    @Override
    public void updateEmail(UpdateEmailDto dto, String oldEmail) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistException(EMAIL_ALREADY_EXIST_MESSAGE.formatted(dto.email()));
        }

        userRepository.updateEmail(oldEmail, dto.email());
        UpdateEmailMessage message = new UpdateEmailMessage(oldEmail, dto.email());

        userMessageProducer.sendUpdateEmailMessage(message);
    }

    @Override
    public void updatePassword(UpdatePasswordDto dto, String email) {
        userRepository.updatePassword(email, passwordEncoder.encode(dto.password()));
    }

    @Override
    public void deleteUser(String email) {
        userRepository.deleteUser(email);

        DeleteUserMessage message = new DeleteUserMessage(email);
        userMessageProducer.sendDeleteUserMessage(message);
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.deleteUserById(id);

        DeleteUserMessage message = new DeleteUserMessage(id.toString());
        userMessageProducer.sendDeleteUserMessage(message);
    }
}
