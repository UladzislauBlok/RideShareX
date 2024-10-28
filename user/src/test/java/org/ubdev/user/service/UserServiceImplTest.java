package org.ubdev.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.kafka.model.user.UploadImageMessage;
import org.ubdev.kafka.producer.KafkaImageMessageProducer;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.exception.exceptions.UserNotFoundException;
import org.ubdev.user.mapper.UserMapper;
import org.ubdev.user.model.User;
import org.ubdev.user.repository.UserRepository;
import org.ubdev.util.cache.UserCacheService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.ubdev.user.model.Preference.Neutral;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private UserMapper userMapper;

    @Mock
    private UserCacheService userCacheService;

    @Mock
    private KafkaImageMessageProducer kafkaImageMessageProducer;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void getUserByEmail_shouldReturnExpectedUserDto() {
        //given
        User user = createTestUser();
        UserDto expectedUserDto = createTestUserDto();
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(expectedUserDto);

        //when
        UserDto actual = userServiceImpl.getUserByEmail("john.doe@example.com");

        //then
        assertEquals(expectedUserDto, actual);
    }

    @Test
    void getUserByEmail_shouldThrownExceptionWhenUserNotFound() {
        //given
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        //when + then
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserByEmail("john.doe@example.com"));
    }

    @ParameterizedTest()
    @MethodSource("getAllInput")
    void getAll_shouldReturnExpectedResult(int pageNum, int pageSize, List<User> userList, List<UserDto> expectedUserDtoList) {
        //given
        when(userRepository.findAll(PageRequest.of(pageNum, pageSize))).thenReturn(new PageImpl<>(userList));
        when(userMapper.userToUserDto(createTestUser())).thenReturn(createTestUserDto());

        //when
        Page<UserDto> actual = userServiceImpl.getAll(pageNum, pageSize);

        //then
        assertThat(actual.getContent()).containsExactlyElementsOf(expectedUserDtoList);
    }

    static Stream<Arguments> getAllInput() {
        return Stream.of(
                Arguments.of(1, 5, List.of(createTestUser()), List.of(createTestUserDto())),
                Arguments.of(1, 5, List.of(), List.of())
        );
    }

    @Test
    void updatePhoto_shouldCallKafkaProducer() {
        //given
        when(userRepository.findIdByEmail("john.doe@example.com")).thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        MultipartFile file = mock(MultipartFile.class);
        UploadImageMessage message = mock(UploadImageMessage.class);
        when(userMapper.mapCreateRequestToUploadImageMessage(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), file)).thenReturn(message);

        //when
        userServiceImpl.updatePhoto(file, "john.doe@example.com");

        //then
        verify(kafkaImageMessageProducer).sendImageMessage(message);
    }

    private static User createTestUser() {
        return User.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .registrationDate(LocalDate.of(2024, 12, 12))
                .photoPath("/photos/johndoe.jpg")
                .phoneNumber("+1-202-555-555")
                .animalPreference(Neutral)
                .conversationPreference(Neutral)
                .musicPreference(Neutral)
                .documents(new ArrayList<>())
                .cars(new ArrayList<>())
                .ratings(new ArrayList<>())
                .build();
    }

    private static UserDto createTestUserDto() {
        return new UserDto(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "John",
                "Doe",
                LocalDate.of(2024, 12, 12),
                "/photos/johndoe.jpg",
                "+1-202-555-555",
                Neutral,
                Neutral,
                Neutral
        );
    }


}