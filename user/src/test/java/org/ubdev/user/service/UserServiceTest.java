package org.ubdev.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.exception.exceptions.DuplicateEmailException;
import org.ubdev.user.exception.exceptions.UserNotFoundException;
import org.ubdev.user.mapper.UserMapper;
import org.ubdev.user.model.User;
import org.ubdev.user.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.ubdev.user.constraint.UserTestConstants.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void getById_ShouldReturnCorrectDto_WhenIdIsCorrect() {
        User user = buildUser();
        UserDto expected = buildUserDto();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(expected);

        UserDto actual = userService.getById(id);
        assertEquals(expected, actual);
    }

    @Test
    public void getById_ShouldThrownException_WhenIdIsIncorrect() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(id));
    }

    @Test
    public void getByEmail_ShouldReturnCorrectDto_WhenIdIsCorrect() {
        User user = buildUser();
        UserDto expected = buildUserDto();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(expected);

        UserDto actual = userService.getByEmail(email);
        assertEquals(expected, actual);
    }

    @Test
    public void getByEmail_ShouldThrownException_WhenIdIsIncorrect() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getByEmail(email));
    }

    @Test
    public void getAll_ShouldReturnCorrectPage() {
        User user = buildUser();
        UserDto expectedUserDto = buildUserDto();
        Page<User> returnValue = new PageImpl<>(
                range(0, 5)
                        .mapToObj(n -> user)
                        .toList()
        );
        when(userRepository.findAll(PageRequest.of(0, 5))).thenReturn(returnValue);
        when(userMapper.userToUserDto(user)).thenReturn(expectedUserDto);

        Page<UserDto> actual = userService.getAll(0,5);
        assertThat(actual.getContent()).hasSize(5);
        assertThat(actual.getContent().get(0)).isEqualTo(expectedUserDto);
        assertThat(actual.getTotalPages()).isEqualTo(1);
        assertThat(actual.getTotalElements()).isEqualTo(5);
    }

    @Test
    public void create_ShouldSaveInRepoAndReturnDto_WhenInputIsCorrect() {
        CreateUserDto inputDto = buildCreateUserDto();
        User userAfterMapping = buildUserWithoutIdAndPhotoUrl();
        User afterSaveInDb = buildUser();
        UserDto expected = buildUserDto();
        when(userMapper.createNewUserWithoutId(inputDto)).thenReturn(userAfterMapping);
        when(userRepository.save(userAfterMapping)).thenReturn(afterSaveInDb);
        when(userMapper.userToUserDto(afterSaveInDb)).thenReturn(expected);

        UserDto actual = userService.create(inputDto);
        assertEquals(expected, actual);
        verify(userRepository).save(userAfterMapping);
    }

    @Test
    public void create_ShouldThrownException_WhenInputIsIncorrect() {
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.create(buildCreateUserDto()));
    }

    @Test
    public void delete_ShouldInvokeDeleteMethod_WhenInputIsCorrect() {
        when(userRepository.existsById(id)).thenReturn(false);

        userService.deleteById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    public void delete_ShouldThrownException_WhenInputIsIncorrect() {
        when(userRepository.existsById(id)).thenReturn(true);

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(id));
    }

    @Test
    public void update_ShouldUpdateAndSaveEntity_WhenInputIsCorrect() {
        User user = buildUser();
        UserUpdateDto userUpdateDto = buildUserUpdateDto();
        UserDto expected = buildUserDto();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromUserDto(userUpdateDto, user);
        when(userMapper.userToUserDto(user)).thenReturn(expected);

        UserDto actual = userService.update(userUpdateDto);
        assertEquals(expected, actual);
        verify(userMapper).updateUserFromUserDto(userUpdateDto, user);
        verify(userRepository).save(user);
    }

    @Test
    public void update_ShouldThrownException_WhenInputIsIncorrect() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(buildUserUpdateDto()));
    }
}
