package ru.practicum.events.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface EventServicePrivate {
    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventByIdAndUserId(Long userId, Long eventId);

    EventFullDto updateEventByIdAndUserId(
            Long userId, Long eventId, UpdateEventUserRequest updateEventAdminRequest);

    List<ParticipationRequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequestByUserIdAndEventId(
            Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);
}
