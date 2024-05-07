package org.ubdev.user.dto;

import jakarta.validation.constraints.Email;

public record UpdateEmailDto (
        @Email
        String email
) {
}
