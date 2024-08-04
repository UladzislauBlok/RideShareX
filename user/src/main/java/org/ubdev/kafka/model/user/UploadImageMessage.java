package org.ubdev.kafka.model.user;

import java.util.UUID;

public record UploadImageMessage(
        UUID id,
        byte[] imageBytes
) {
}
