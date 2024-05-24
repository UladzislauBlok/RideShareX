package org.ubdev.kafka.model;

public record DeleteUserMessage(String email) implements KafkaUserMessage {
}
