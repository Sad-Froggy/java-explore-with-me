package ru.practicum.requests.service;

import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;


public interface RequestService {

    ParticipationRequestDto addRequest(Long eventId, Long userId);

    List<ParticipationRequest> findConfirmedRequests(List<Event> events);

    ParticipationRequestDto cancelRequest(Long requestId, Long userId);

    List<ParticipationRequestDto> allUserRequests(Long userId);

    List<ParticipationRequestDto> getEventRequest(Long eventId, Long userId);

    EventRequestStatusUpdateResult update(Long eventId, Long userId, EventRequestStatusUpdateRequest request);

}
