package org.ubdev.document.dto;

import org.ubdev.document.model.DocumentType;

import java.time.LocalDate;

public record DocumentCreateDto (
        DocumentType type,
        String number,
        LocalDate creationDate
) {
}
