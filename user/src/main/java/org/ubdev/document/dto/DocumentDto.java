package org.ubdev.document.dto;

import org.ubdev.document.model.DocumentType;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentDto (
    UUID id,
    UUID user_id,
    DocumentType type,
    String number,
    LocalDate creationDate
) {
}
