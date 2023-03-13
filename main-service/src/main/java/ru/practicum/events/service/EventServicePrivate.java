package ru.practicum.events.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface EventServicePrivate {
    @Transactional
    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    @Transactional
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventByIdAndUserId(Long userId, Long eventId);

    @Transactional
    EventFullDto updateEventByIdAndUserId(
            Long userId, Long eventId, UpdateEventUserRequest updateEventAdminRequest);

    @Transactional
    List<ParticipationRequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId);

    @Transactional
    EventRequestStatusUpdateResult updateStatusRequestByUserIdAndEventId(
            Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);
}
