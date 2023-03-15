package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.*;
import ru.practicum.events.service.EventServicePrivate;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {
    private final EventServicePrivate eventServicePriv;

    @GetMapping
    public List<EventShortDto> getByUserId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос событий созданных пользователем с id - " + userId);
        return eventServicePriv.getEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Запрос добавления нового события от пользователя с id - " + userId);
        return eventServicePriv.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getFullInfoByEventIdAndUserId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId) {
        log.info("Запрос информации о событии с id - {} созданным пользователем с id - {}", eventId, userId);
        return eventServicePriv.getEventByIdAndUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId,
            @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Запрос изменения события с id - {} добавленного пользователем с id - {}", eventId, userId);
        return eventServicePriv.updateEventByIdAndUserId(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId) {
        log.info("Запрос информации о запросах на участие в событии с id - {} ползователя с id - {}", eventId, userId);
        return eventServicePriv.getRequestByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId,
            @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("Изменение статуса заявок на участие в событии с id - {} пользователя с id - {} ", eventId, userId);
        return eventServicePriv.updateStatusRequestByUserIdAndEventId(userId, eventId, statusUpdateRequest);
    }
}
