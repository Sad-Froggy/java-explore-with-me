package ru.practicum.users.service;

//TODO исключение

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getByIds(Collection<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        Iterable<User> users;
        if (ids != null) {
            users = userRepository.findByIdIn(ids, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        Collection<UserDto> usersDto = new ArrayList<>();
        users.forEach(user -> usersDto.add(UserMapper.toUserDto(user)));
        return usersDto;
    }

    @Transactional
    @Override
    public ResponseEntity<UserDto> add(NewUserRequest newUserRequest) {
        User user = userRepository.save(UserMapper.toUser(newUserRequest));
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity<Object> delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("позже создать своё исключение"));
        userRepository.deleteById(userId);
        return ResponseEntity.status(204).build();
    }
}
