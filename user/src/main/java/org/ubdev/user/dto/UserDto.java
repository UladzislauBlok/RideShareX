package org.ubdev.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ubdev.user.model.Preference;

import java.time.LocalDate;
import java.util.UUID;

public record UserDto (
        UUID id,
        String name,
        String surname,
        String email,
        @JsonProperty("registration_date")
        LocalDate registrationDate,

        @JsonProperty("photo_url")
        String photoUrl,

        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("animal_preference")
        Preference animalPreference,

        @JsonProperty("conversation_preference")
        Preference conversationPreference,

        @JsonProperty("music_preference")
        Preference musicPreference) {
}
