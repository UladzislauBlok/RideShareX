package org.ubdev.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.user.model.Preference;

import java.time.LocalDate;

public record CreateUserDto(
        @NotBlank(message = "Name is a required field")
        String name,
        @NotBlank(message = "Surname is a required field")
        String surname,
        @Email
        String email,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                message = "Password has not been validated")
        String password,

        @JsonProperty("registration_date")
        LocalDate registrationDate,
        String photoBase64,

        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("animal_preference")
        Preference animalPreference,

        @JsonProperty("conversation_preference")
        Preference conversationPreference,

        @JsonProperty("music_preference")
        Preference musicPreference) {

}
