package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;


public interface RequestService {

    List<ParticipationRequestDto> getByUserId(Long userId);

    ParticipationRequestDto createByUserId(Long userId, Long eventId);

    ParticipationRequestDto cancellationByUserIdAndRequestId(Long userId, Long requestId);
}
