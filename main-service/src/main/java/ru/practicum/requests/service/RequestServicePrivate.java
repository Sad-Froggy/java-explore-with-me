package ru.practicum.requests.service;

import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;


public interface RequestServicePrivate {

    List<ParticipationRequestDto> getRequestByUserId(Long userId);

    ParticipationRequestDto createRequestByUserId(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByIdAndUserId(Long userId, Long requestId);

    List<ParticipationRequest> findConfirmedRequests(List<Event> events);
}
