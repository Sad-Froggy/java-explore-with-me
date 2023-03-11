package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> get(@RequestParam(value = "ids", required = false, defaultValue = "0") List<Long> ids,
                                   @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                   @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getByIds(ids, from, size);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        return userService.delete(userId);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid NewUserRequest request) {
        return userService.add(request);
    }

}
