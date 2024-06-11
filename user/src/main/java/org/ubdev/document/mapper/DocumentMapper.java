package org.ubdev.document.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.ubdev.document.dto.DocumentCreateDto;
import org.ubdev.document.dto.DocumentDto;
import org.ubdev.document.model.Document;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(target = "user_id", source = "document.user.id")
    DocumentDto mapToDto(Document document);

    @Mappings({
            @Mapping(target = "user", expression = "java(null)"),
            @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    })
    Document mapCreateDtoToDocument(DocumentCreateDto dto);
}