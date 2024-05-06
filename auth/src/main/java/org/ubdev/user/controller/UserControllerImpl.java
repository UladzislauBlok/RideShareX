package org.ubdev.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.service.UserService;

import static org.ubdev.user.config.UserConstants.USER_SUCCESSFULLY_CREATED_MESSAGE;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @PostMapping
    @Override
    public ResponseEntity<String> createUser(@RequestPart CreateUserDto createUserDto,
                                             @RequestPart MultipartFile file) {
        log.info("Create user: {}", createUserDto);
        userService.createUser(createUserDto, file);
        return new ResponseEntity<>(USER_SUCCESSFULLY_CREATED_MESSAGE.formatted(createUserDto.email()), HttpStatus.CREATED);
    }
}
