package org.ubdev.user.dto;

import jakarta.validation.constraints.Pattern;

public record UpdatePasswordDto (
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                message = "Password has not been validated")
        String password
) {
}
