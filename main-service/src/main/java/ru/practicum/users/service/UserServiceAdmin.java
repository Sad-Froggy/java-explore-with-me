package ru.practicum.users.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserServiceAdmin {
    List<UserDto> getUsersByIds(List<Long> ids, int from, int size);

    ResponseEntity<UserDto> createUser(NewUserRequest newUserRequest);

    ResponseEntity<Object> deleteUser(Long userId);

}