package org.ubdev.kafka.model;

import java.util.UUID;

public record UploadImageMessage(
        UUID id,
        byte[] imageBytes
) {
}
