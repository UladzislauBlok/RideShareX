package org.ubdev.user.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ubdev.user.constraint.UserTestConstants.*;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void userMapper_ShouldReturnCorrectUserDto_FromUser() {
        User user = buildUser();
        UserDto expected = buildUserDto();

        UserDto actual = userMapper.userToUserDto(user);
        assertEquals(expected, actual);
    }

    @Test
    void userMapper_ShouldReturnCorrectUser_FromUserDto() {
        CreateUserDto input = buildCreateUserDto();
        User expected = buildUserWithoutIdAndPhotoUrl();

        User actual = userMapper.createNewUserWithoutId(input);
        assertEquals(expected, actual);
    }

    @Test
    void userMapper_ShouldCorrectUpdate_FromUserUpdateDto() {
        User actual = buildUser();
        UserUpdateDto dto = buildUserUpdateDto();
        User expected = buildUser();
        expected.setName("NewName");
        expected.setSurname("NewSurname");

        userMapper.updateUserFromUserDto(dto, actual);
        assertEquals(expected, actual);
    }

    private UserUpdateDto buildUserUpdateDto() {
        return new UserUpdateDto(
                id,
                "NewName",
                "NewSurname",
                null,
                phoneNumber,
                animalPreference,
                conversationPreference,
                musicPreference
        );
    }

    private User buildUserWithoutIdAndPhotoUrl() {
        return User.builder()
                .id(null)
                .name(name)
                .surname(surname)
                .email(email)
                .password(password)
                .registrationDate(registrationDate)
                .photoUrl(null)
                .phoneNumber(phoneNumber)
                .animalPreference(animalPreference)
                .conversationPreference(conversationPreference)
                .musicPreference(musicPreference)
                .build();
    }

    private CreateUserDto buildCreateUserDto() {
        return new CreateUserDto(
                name,
                surname,
                email,
                password,
                registrationDate,
                null,
                phoneNumber,
                animalPreference,
                conversationPreference,
                musicPreference
        );
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