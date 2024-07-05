package org.blokdev.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.blokdev.kafka.model.JoinTripMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripMessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.producer.trip-topic.name}")
    private String topicName;

    public void sendJoinTripMessage(JoinTripMessage message) {
        kafkaTemplate.send(topicName, message);
    }
}
