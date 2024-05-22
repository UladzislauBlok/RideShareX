package org.ubdev.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UpdateEmailDto;
import org.ubdev.user.dto.UpdatePasswordDto;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public interface UserController {
    ResponseEntity<Map> createUser(CreateUserDto createUserDto, MultipartFile file);
    ResponseEntity<Map> updateEmail(UpdateEmailDto dto, Principal principal);
    ResponseEntity<Map> updatePassword(UpdatePasswordDto dto, Principal principal);
    ResponseEntity<Map> deleteUser(Principal principal);
    ResponseEntity<Map> deleteUser(UUID id);
}
