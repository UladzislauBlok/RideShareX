package org.ubdev.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.user.dto.CreateUserDto;

public interface UserController {
    ResponseEntity<String> createUser(CreateUserDto createUserDto, MultipartFile file);
}
