package org.ubdev.kafka.model.user;

public record UpdateEmailMessage (
        String oldEmail,
        String newEmail
) implements KafkaUserMessage {
}
