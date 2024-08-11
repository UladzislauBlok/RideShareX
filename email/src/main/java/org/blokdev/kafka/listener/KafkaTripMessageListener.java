package org.blokdev.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.blokdev.kafka.model.JoinTripMessage;
import org.blokdev.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.blokdev.kafka.config.KafkaConstants.UNSUPPORTED_KAFKA_MESSAGE_TYPE;

@Slf4j
@RequiredArgsConstructor
public class KafkaTripMessageListener {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.consumer.trip-topic.name}", groupId = "${kafka.consumer.trip-topic.consumer-group-id}")
    public void listen(ConsumerRecord<String, Object> record) {
        Object message = record.value();
        executorService.submit(() -> processMessage(message));
    }

    private void processMessage(Object message) {
        if (message instanceof JoinTripMessage tripMessage) {
            emailService.sendSimpleMessage(tripMessage);
        } else {
            log.error(UNSUPPORTED_KAFKA_MESSAGE_TYPE.formatted(message));
        }
    }
}
