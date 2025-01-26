package ru.practicum.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AdminUsersController {
    private final UserService service;

    @GetMapping
    public List<UserDto> findAllUsers(@RequestParam(required = false) List<Long> ids,
                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("==> findAllUsers ids={}, from={}, size={}", ids, from, size);
        return service.findAllUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid
                              @RequestBody NewUserRequest requestBody) {
        log.info("==> createUser requestBody={}", requestBody);
        return service.createUser(requestBody);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@NotNull
                           @PathVariable long userId) {
        log.info("==> deleteUser requestBody={}", userId);
        service.deleteUser(userId);
    }
}
