package org.ubdev.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ubdev.user.model.Preference;

public record UserUpdateDto (
        String name,
        String surname,

        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("animal_preference")
        Preference animalPreference,

        @JsonProperty("conversation_preference")
        Preference conversationPreference,

        @JsonProperty("music_preference")
        Preference musicPreference) {
}
