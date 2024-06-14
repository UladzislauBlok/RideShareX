package org.ubdev.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ubdev.user.dto.CreateUserDto;
import org.ubdev.user.dto.UpdateEmailDto;
import org.ubdev.user.dto.UpdatePasswordDto;
import org.ubdev.user.service.UserService;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import static org.ubdev.user.config.UserConstants.USER_SUCCESSFULLY_CREATED_MESSAGE;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @PostMapping
    @Override
    public ResponseEntity<Map> createUser(@RequestPart CreateUserDto createUserDto,
                                             @RequestPart MultipartFile file) {
        log.info("Create user: {}", createUserDto);
        userService.createUser(createUserDto, file);
        Map<String, String> response = Map.of(
                "message", USER_SUCCESSFULLY_CREATED_MESSAGE.formatted(createUserDto.email())
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/email")
    @Override
    public ResponseEntity<Map> updateEmail(@RequestBody UpdateEmailDto dto, Principal principal) {
        userService.updateEmail(dto, principal.getName());
        return ResponseEntity.ok(Map.of("message", "OK"));
    }

    @PatchMapping("/password")
    @Override
    public ResponseEntity<Map> updatePassword(@RequestBody UpdatePasswordDto dto, Principal principal) {
        userService.updatePassword(dto, principal.getName());
        return ResponseEntity.ok(Map.of("message", "OK"));
    }

    @DeleteMapping
    @Override
    public ResponseEntity<Map> deleteUser(Principal principal) {
        userService.deleteUser(principal.getName());
        return new ResponseEntity(Map.of("message", "OK"), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Map> deleteUser(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return new ResponseEntity(Map.of("message", "OK"), HttpStatus.NO_CONTENT);
    }
}
