package org.ubdev.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.mapper.UserMapper;
import org.ubdev.user.model.User;
import org.ubdev.user.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.ubdev.user.constraint.UserTestConstants.*;

public class UserServiceTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    public void getById_ShouldReturnCorrectDto_WhenIdIsCorrect() {
        when(userRepository.findById(id)).thenReturn(Optional.of(buildUser()));
        UserDto expected = buildUserDto();

        UserDto actual = userService.getById(id);
        assertEquals(expected, actual);
    }

    @Test
    public void getById_ShouldThrownException_WhenIdIsIncorrect() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getById(id));
    }

    private UserDto buildUserDto() {
        return new UserDto(
                id,
                name,
                surname,
                email,
                registrationDate,
                photoUrl,
                phoneNumber,
                animalPreference,
                conversationPreference,
                musicPreference
        );
    }

    private User buildUser() {
        return User.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .email(email)
                .password(password)
                .registrationDate(registrationDate)
                .photoUrl(photoUrl)
                .phoneNumber(phoneNumber)
                .animalPreference(animalPreference)
                .conversationPreference(conversationPreference)
                .musicPreference(musicPreference)
                .build();
    }
}
