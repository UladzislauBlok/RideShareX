package org.ubdev.user.service;

import org.springframework.web.multipart.MultipartFile;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UpdateEmailDto;
import org.ubdev.user.dto.UpdatePasswordDto;

import java.util.UUID;

public interface UserService {
    void createUser(CreateUserDto dto, MultipartFile image);
    void updateEmail(UpdateEmailDto dto, String oldEmail);
    void updatePassword(UpdatePasswordDto dto, String email);
    void deleteUser(String email);
    void deleteUserById(UUID id);
}
