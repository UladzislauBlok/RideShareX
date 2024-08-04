package org.ubdev.service;

import org.ubdev.kafka.model.UploadImageMessage;

import java.util.UUID;

public interface AWSService {
    void saveImage(UploadImageMessage imageMessage);

    byte[] getImage(UUID id);
}
