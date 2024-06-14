package org.ubdev.document.dto;

import org.ubdev.document.model.DocumentType;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentDto (
    UUID id,
    DocumentType type,
    String number,
    LocalDate creationDate
) {
}
