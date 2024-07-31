package org.ubdev.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.ubdev.kafka.model.CreateUserMessage;
import org.ubdev.kafka.model.DeleteUserMessage;
import org.ubdev.kafka.model.UpdateEmailMessage;

@Service
@RequiredArgsConstructor
public class KafkaUserMessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.producer.user-topic.name}")
    private String topicName;


    public void sendCreateUserMessage(CreateUserMessage message) {
        kafkaTemplate.send(topicName, message);
    }

    public void sendDeleteUserMessage(DeleteUserMessage message) {
        kafkaTemplate.send(topicName, message);
    }

    public void sendUpdateEmailMessage(UpdateEmailMessage message) {
        kafkaTemplate.send(topicName, message);
    }
}

