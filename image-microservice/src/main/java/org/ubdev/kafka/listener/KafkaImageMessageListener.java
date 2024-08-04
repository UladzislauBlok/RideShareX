package org.ubdev.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.ubdev.kafka.model.UploadImageMessage;
import org.ubdev.service.AWSService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.ubdev.kafka.config.KafkaConstants.UNSUPPORTED_KAFKA_MESSAGE_TYPE;

@Slf4j
@RequiredArgsConstructor
public class KafkaImageMessageListener {
    private final AWSService awsService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @KafkaListener(topics = "${kafka.consumer.image-topic.name}", groupId = "${kafka.consumer.image-topic.consumer-group-id}")
    public void listen(ConsumerRecord<String, Object> record) {
        Object message = record.value();
        executorService.submit(() -> processMessage(message));
    }

    private void processMessage(Object message) {
        if (message instanceof UploadImageMessage imageMessage) {
            awsService.saveImage(imageMessage);
        } else {
            log.error(UNSUPPORTED_KAFKA_MESSAGE_TYPE.formatted(message));
        }
    }
}
