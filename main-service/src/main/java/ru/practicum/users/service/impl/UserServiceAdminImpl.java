package ru.practicum.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DataConflictException;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.users.service.UserServiceAdmin;
import ru.practicum.util.EwmObjectFinder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceAdminImpl implements UserServiceAdmin {

    private final UserRepository userRepository;

    private final EwmObjectFinder finder;

    @Override
    public List<UserDto> getUsersByIds(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);

        if (ids.isEmpty()) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findByIdIn(ids, pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        try {
            User user = userRepository.save(UserMapper.toUser(newUserRequest));
            return UserMapper.toUserDto(user);
        } catch (Exception e) {
            throw new DataConflictException("Пользователь с таким email уже существует - " + newUserRequest.getEmail());
        }
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        finder.checkUserExistenceById(userId);
        userRepository.deleteById(userId);
    }
}
