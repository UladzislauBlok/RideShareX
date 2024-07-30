package org.ubdev.kafka.service;

import org.ubdev.kafka.model.UploadImageMessage;

public interface AWSService {
    void saveImage(UploadImageMessage imageMessage);
}
