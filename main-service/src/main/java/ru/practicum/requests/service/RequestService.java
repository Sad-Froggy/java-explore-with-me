package ru.practicum.requests.service;

import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;


public interface RequestService {

    List<ParticipationRequestDto> getByUserId(Long userId);

    ParticipationRequestDto createByUserId(Long userId, Long eventId);

    ParticipationRequestDto cancellationByUserIdAndRequestId(Long userId, Long requestId);
}
