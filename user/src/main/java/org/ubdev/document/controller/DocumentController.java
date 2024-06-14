package org.ubdev.document.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ubdev.document.dto.DocumentCreateDto;
import org.ubdev.document.dto.DocumentDto;
import org.ubdev.document.service.DocumentService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<DocumentDto>> getDocumentByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(documentService.getDocumentsByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<DocumentDto>> getDocumentByCurrentUser(Principal principal) {
        return ResponseEntity.ok(documentService.getDocumentsByCurrentUser(principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocumentById(@PathVariable UUID id) {
        documentService.deleteDocumentById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<?> createDocument(@RequestBody DocumentCreateDto dto, Principal principal) {
        return ResponseEntity.ok(documentService.createDocument(dto, principal.getName()));
    }
}
