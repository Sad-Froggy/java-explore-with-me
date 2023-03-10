package ru.practicum.requests.service;

import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestStatus;

import java.util.List;


public interface RequestService {

    public ParticipationRequestDto addRequest(Long eventId, Long userId);

    public List<ParticipationRequest> findConfirmedRequests(List<Event> events);

    public ParticipationRequestDto cancelRequest(Long requestId, Long userId);

    public List<ParticipationRequestDto> allUserRequests(Long userId);

    public List<ParticipationRequestDto> getEventRequest(Long eventId, Long userId);

    public EventRequestStatusUpdateResult update(Long eventId, Long userId, EventRequestStatusUpdateRequest request);

}
