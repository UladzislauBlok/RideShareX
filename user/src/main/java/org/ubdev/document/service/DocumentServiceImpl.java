package org.ubdev.document.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ubdev.document.dto.DocumentCreateDto;
import org.ubdev.document.dto.DocumentDto;
import org.ubdev.document.exception.DocumentNotFoundException;
import org.ubdev.document.mapper.DocumentMapper;
import org.ubdev.document.model.Document;
import org.ubdev.document.repository.DocumentRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Override
    public DocumentDto getDocumentById(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(DocumentNotFoundException::new);

        return documentMapper.mapToDto(document);
    }

    @Override
    public List<DocumentDto> getDocumentsByUserId(UUID userId) {
        return documentRepository.findAllByUserId(userId)
                .stream()
                .map(documentMapper::mapToDto)
                .toList();
    }

    @Override
    public List<DocumentDto> getDocumentsByCurrentUser(String userEmail) {
        return documentRepository.findAllDocumentsByUserEmail(userEmail);
    }

    @Override
    public DocumentDto createDocument(DocumentCreateDto dto, String userEmail) {
        Document document = documentMapper.mapCreateDtoToDocument(dto);
        return documentRepository.saveDocumentWithUserEmail(document, userEmail);
    }

    @Override
    public void deleteDocumentById(UUID id) {
        documentRepository.deleteById(id);
    }
}
