package org.blokdev.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.blokdev.kafka.model.CreateRatingMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingMessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.producer.rating-topic.name}")
    private String topicName;

    public void sendRatingMessage(CreateRatingMessage message) {
        kafkaTemplate.send(topicName, message);
    }
}
