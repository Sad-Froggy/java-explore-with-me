package ru.practicum.users.mapper;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

public class UserMapper {

    public static UserShortDto toUserShortDto(User source) {
        return UserShortDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    public static User toUser(NewUserRequest source) {
        return User.builder()
                .email(source.getEmail())
                .name(source.getName())
                .build();
    }

    public static UserDto toUserDto(User source) {
        return UserDto.builder()
                .id(source.getId())
                .email(source.getEmail())
                .name(source.getName())
                .build();
    }
}
