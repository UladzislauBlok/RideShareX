package org.ubdev.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.ubdev.user.model.Preference;

public record CreateUserDto(
        @Email
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                message = "Password has not been validated")
        String password,

        @NotBlank(message = "Name is a required field")
        String name,

        @NotBlank(message = "Surname is a required field")
        String surname,
        @JsonProperty("phone_number")
        String phoneNumber,
        @JsonProperty("animal_preference")
        Preference animalPreference,
        @JsonProperty("conversation_preference")
        Preference conversationPreference,
        @JsonProperty("music_preference")
        Preference musicPreference)
{
}
