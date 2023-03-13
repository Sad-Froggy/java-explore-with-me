package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserControllerAdmin {

    private final UserServiceAdmin userServiceAdmin;

    @GetMapping
    public List<UserDto> get(@RequestParam(value = "ids", required = false, defaultValue = "0") List<Long> ids,
                                   @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                   @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Запрос получения информации о пользователях от администратора");
        return userServiceAdmin.getUsersByIds(ids, from, size);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("Запрос удаления пользователя с id {} от администратора", userId);
        return userServiceAdmin.deleteUser(userId);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid NewUserRequest request) {
        log.info("Запрос добавления нового пользователя от администратора");
        return userServiceAdmin.createUser(request);
    }

}
