package org.ubdev.kafka.model;

public record UpdateEmailMessage (
        String oldEmail,
        String newEmail
) {
}
