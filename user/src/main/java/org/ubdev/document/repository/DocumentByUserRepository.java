package org.ubdev.document.repository;

import org.ubdev.document.dto.DocumentDto;
import org.ubdev.document.model.Document;

import java.util.List;

public interface DocumentByUserRepository {
    DocumentDto saveDocumentWithUserEmail(Document document, String userEmail);
    List<DocumentDto> findAllDocumentsByUserEmail(String userEmail);
}
