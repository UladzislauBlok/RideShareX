package org.ubdev.kafka.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ubdev.user.model.Preference;

import java.util.UUID;

public record CreateUserMessage (
        UUID id,
        String name,
        String surname,
        String email,
        String photoPath,

        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("animal_preference")
        Preference animalPreference,

        @JsonProperty("conversation_preference")
        Preference conversationPreference,

        @JsonProperty("music_preference")
        Preference musicPreference) implements KafkaUserMessage {
}
