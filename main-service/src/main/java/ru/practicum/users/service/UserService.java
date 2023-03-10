package ru.practicum.users.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Collection<UserDto> getByIds(List<Long> ids, int from, int size);

    ResponseEntity<UserDto> add(NewUserRequest newUserRequest);

    ResponseEntity<Object> delete(Long userId);
}