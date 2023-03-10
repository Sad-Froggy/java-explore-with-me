package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> get(@RequestParam(required = false, defaultValue = "0") List<Long> ids,
                                   @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                   @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getByIds(ids, from, size);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        return userService.delete(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid NewUserRequest request) {
        return new ResponseEntity<>(userService.add(request), HttpStatus.CREATED);
    }

}
