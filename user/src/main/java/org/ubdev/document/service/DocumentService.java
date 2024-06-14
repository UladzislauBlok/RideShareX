package org.ubdev.document.service;

import org.ubdev.document.dto.DocumentCreateDto;
import org.ubdev.document.dto.DocumentDto;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    DocumentDto getDocumentById(UUID id);
    List<DocumentDto> getDocumentsByUserId(UUID userId);
    List<DocumentDto> getDocumentsByUser(String userEmail);
    DocumentDto createDocument(DocumentCreateDto dto, String userEmail);
    void deleteDocumentById(UUID id);
}
