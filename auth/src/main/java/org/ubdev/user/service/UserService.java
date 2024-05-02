package org.ubdev.user.service;

import org.springframework.web.multipart.MultipartFile;
import org.ubdev.user.dto.CreateUserDto;

public interface UserService {
    void createUser(CreateUserDto dto, MultipartFile image);
}
