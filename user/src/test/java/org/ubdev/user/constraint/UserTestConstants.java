package org.ubdev.user.constraint;

import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.model.Preference;
import org.ubdev.user.model.User;

import java.time.LocalDate;
import java.util.UUID;

public class UserTestConstants {
    public static final UUID id = UUID.fromString("74b7d2b2-004c-45df-b0bd-38d2e9f37d72");
    public static final String name = "TestName";
    public static final String surname = "TestSurname";
    public static final String email = "test@email.com";
    public static final String password = "testpassword";
    public static final LocalDate registrationDate = LocalDate.of(2002, 12,31);
    public static final String photoUrl = "userPhotoUrl";
    public static final String phoneNumber = "375(29)7053894";
    public static final Preference animalPreference = Preference.Neutral;
    public static final Preference conversationPreference = Preference.Negative;
    public static final Preference musicPreference = Preference.Positive;

    public static UserUpdateDto buildUserUpdateDto() {
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

    public static User buildUserWithoutIdAndPhotoUrl() {
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

    public static CreateUserDto buildCreateUserDto() {
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

    public static UserDto buildUserDto() {
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

    public static User buildUser() {
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
