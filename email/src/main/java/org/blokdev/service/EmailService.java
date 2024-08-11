package org.blokdev.service;

import lombok.RequiredArgsConstructor;
import org.blokdev.kafka.model.JoinTripMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailService {
    private static final String EMAIL_PATTERN = "User %s with id %s tries to join trip | attempt id: %s";

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(JoinTripMessage kafkaMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@radesharex.com");
        message.setTo(kafkaMessage.ownerEmail());
        message.setSubject("Trip | Join Request");
        message.setText(EMAIL_PATTERN.formatted(kafkaMessage.attemptUsername(), kafkaMessage.attemptUserId(), kafkaMessage.attemptId()));
        emailSender.send(message);
    }
}
