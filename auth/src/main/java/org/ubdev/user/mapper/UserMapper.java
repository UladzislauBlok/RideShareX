package org.ubdev.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.kafka.model.UploadImageMessage;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.kafka.model.CreateUserMessage;
import org.ubdev.user.exception.ImageProcessingException;
import org.ubdev.user.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", expression = "java( UUID.randomUUID() )"),
            @Mapping(target = "emailConfirmed", expression = "java(false)"),
    })
    User mapCreateRequestToUser(CreateUserDto createUserDto);

    CreateUserMessage mapCreateRequestToCreateUserMessage(CreateUserDto createUserDto, String photoPath, UUID id);

    @Mapping(target = "imageBytes", expression = "java(extractBytesFromImage(image))")
    UploadImageMessage mapCreateRequestToUploadImageMessage(UUID id, MultipartFile image);

    default byte[] extractBytesFromImage(MultipartFile image) {
        try (InputStream inputStream = image.getInputStream()) {
            return inputStream.readAllBytes();
        } catch (IOException exception) {
            throw new ImageProcessingException(exception.getMessage());
        }
    }
}
