package org.ubdev.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.ubdev.kafka.model.UploadImageMessage;

@Service
@RequiredArgsConstructor
public class KafkaImageMessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.producer.image-topic.name}")
    private String topicName;

    public void sendImageMessage(UploadImageMessage message) {
        kafkaTemplate.send(topicName, message);
    }
}
