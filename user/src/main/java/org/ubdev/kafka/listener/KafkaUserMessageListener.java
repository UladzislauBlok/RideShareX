package org.ubdev.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ubdev.kafka.model.user.CreateUserMessage;
import org.ubdev.kafka.model.user.DeleteUserMessage;
import org.ubdev.kafka.model.user.KafkaUserMessage;
import org.ubdev.kafka.model.user.UpdateEmailMessage;
import org.ubdev.user.service.UserService;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.ubdev.kafka.config.KafkaConstants.UNSUPPORTED_KAFKA_MESSAGE_TYPE;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaUserMessageListener {
    private final UserService userService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<Class<? extends KafkaUserMessage>, Consumer<Object>> handlers = Map.of(
            CreateUserMessage.class, message -> handleCreateUserMessage((CreateUserMessage) message),
            DeleteUserMessage.class, message -> handleDeleteUserMessage((DeleteUserMessage) message),
            UpdateEmailMessage.class, message -> handleUpdateEmailMessage((UpdateEmailMessage) message)
    );


    @KafkaListener(topics = "${kafka.consumer.user-topic.name}", groupId = "${kafka.consumer.user-topic.consumer-group-id}")
    public void listen(ConsumerRecord<String, Object> record) {
        Object message = record.value();
        executorService.submit(() -> processMessage(message));
    }

    private void processMessage(Object message) {
        Consumer<Object> handler = handlers.get(message.getClass());

        if (handler != null) {
            handler.accept(message);
        } else {
            log.error(UNSUPPORTED_KAFKA_MESSAGE_TYPE.formatted(message));
        }
    }

    private void handleCreateUserMessage(CreateUserMessage message) {
        userService.create(message);
    }

    private void handleDeleteUserMessage(DeleteUserMessage message) {
        userService.deleteByMessage(message);
    }

    private void handleUpdateEmailMessage(UpdateEmailMessage message) {
        userService.updateEmail(message);
    }
}
