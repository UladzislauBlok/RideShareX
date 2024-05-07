package org.ubdev.user.dto;

public record UpdateEmailMessage (
        String oldEmail,
        String newEmail
) {
}
