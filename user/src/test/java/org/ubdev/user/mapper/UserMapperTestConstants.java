package org.ubdev.user.mapper;

import org.ubdev.user.model.Preference;

import java.time.LocalDate;
import java.util.UUID;

public class UserMapperTestConstants {
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
}
