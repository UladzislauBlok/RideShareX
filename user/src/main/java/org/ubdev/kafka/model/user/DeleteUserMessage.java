package org.ubdev.kafka.model.user;

public record DeleteUserMessage(String email) implements KafkaUserMessage {
}
