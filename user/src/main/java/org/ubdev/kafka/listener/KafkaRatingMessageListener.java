package org.ubdev.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ubdev.kafka.model.rating.CreateRatingMessage;
import org.ubdev.rating.service.RatingService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.ubdev.kafka.config.KafkaConstants.UNSUPPORTED_KAFKA_MESSAGE_TYPE;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaRatingMessageListener {
    private final RatingService ratingService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @KafkaListener(topics = "${kafka.consumer.rating-topic.name}", groupId = "${kafka.consumer.rating-topic.consumer-group-id}")
    public void listen(ConsumerRecord<String, Object> record) {
        Object message = record.value();
        executorService.submit(() -> processMessage(message));
    }

    private void processMessage(Object message) {
        if (message instanceof CreateRatingMessage ratingMessage) {
            ratingService.createRating(ratingMessage);
        } else {
            log.error(UNSUPPORTED_KAFKA_MESSAGE_TYPE.formatted(message));
        }
    }
}

