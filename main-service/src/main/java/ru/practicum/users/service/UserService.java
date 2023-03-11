package ru.practicum.users.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getByIds(List<Long> ids, int from, int size);

    ResponseEntity<UserDto> add(NewUserRequest newUserRequest);

    ResponseEntity<Object> delete(Long userId);

    User getByIdForService(Long userId);
}