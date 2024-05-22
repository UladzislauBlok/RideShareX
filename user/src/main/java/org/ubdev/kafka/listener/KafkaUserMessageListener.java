package org.ubdev.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ubdev.kafka.model.CreateUserMessage;
import org.ubdev.kafka.model.DeleteUserMessage;
import org.ubdev.kafka.model.UpdateEmailMessage;
import org.ubdev.user.service.UserService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.ubdev.kafka.config.KafkaConstants.UNSUPPORTED_KAFKA_MESSAGE_TYPE;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaUserMessageListener {
    private final UserService userService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @KafkaListener(topics = "${kafka.topics.user-topic.name}", groupId = "${kafka.topics.user-topic.group-id}")
    public void listen(ConsumerRecord<String, Object> record) {
        Object input = record.value();
        executorService.submit(() -> processMessage(input));
    }

    private void processMessage(Object input) {
        if (input instanceof CreateUserMessage message) {
            handleCreateUserMessage(message);
        } else if (input instanceof DeleteUserMessage message) {
            handleDeleteUserMessage(message);
        } else if (input instanceof UpdateEmailMessage message) {
            handleUpdateEmailMessage(message);
        }

        log.error(UNSUPPORTED_KAFKA_MESSAGE_TYPE.formatted(input));
    }

    private void handleCreateUserMessage(CreateUserMessage message) {
        userService.create(message);
    }

    private void handleDeleteUserMessage(DeleteUserMessage message) {
        userService.delete(message);
    }

    private void handleUpdateEmailMessage(UpdateEmailMessage message) {
        userService.updateEmail(message);
    }
}
