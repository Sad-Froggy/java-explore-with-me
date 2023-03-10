package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> get(@RequestParam (required = false) Collection<Integer> ids,
                                   @RequestParam (defaultValue = "0") int from,
                                   @RequestParam (defaultValue = "10") int size) {
        return userService.getByIds(ids, from, size);
    }

    @PostMapping
    public ResponseEntity<UserDto> add(@RequestBody @Valid NewUserRequest newUserRequest) {
        return userService.add(newUserRequest);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        return userService.delete(userId);
    }
}
