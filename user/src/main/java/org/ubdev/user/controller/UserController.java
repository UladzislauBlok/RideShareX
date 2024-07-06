package org.ubdev.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.ubdev.user.dto.UserDto;
import org.ubdev.user.dto.UserUpdateDto;
import org.ubdev.user.model.JoinTripRequestData;
import org.ubdev.user.service.UserService;

import java.security.Principal;
import java.util.UUID;

import static org.ubdev.user.controller.ControllerConstants.SYSTEM_HEADER_ERROR_MESSAGE;
import static org.ubdev.user.controller.ControllerConstants.SYSTEM_MESSAGE_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam int page,
                                                  @RequestParam int size) {
        return ResponseEntity.ok(userService.getAll(page, size));
    }

    @GetMapping("/trip")
    public ResponseEntity<JoinTripRequestData> getDataForJoinTripRequest(
            @RequestParam("email") String email,
            @RequestParam("ownerId") UUID ownerId,
            @RequestHeader HttpHeaders headers) {
        if (!headers.containsKey(SYSTEM_MESSAGE_HEADER))
            throw new AccessDeniedException(SYSTEM_HEADER_ERROR_MESSAGE);
        JoinTripRequestData data = userService.getJoinTripRequestData(email, ownerId);

        return ResponseEntity.ok(data);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UUID> getUserIdByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getIdByEmail(email));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto dto, Principal principal) {
        return ResponseEntity.ok(userService.update(dto, principal.getName()));
    }
}
