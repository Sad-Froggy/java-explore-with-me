package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.EventServicePrivate;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventPrivateController {
    private final EventServicePrivate eventServicePriv;

    @GetMapping
    public List<EventShortDto> getByUserId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получение событий добавленных пользователем с id: {}, from {}, size {}", userId, from, size);
        return eventServicePriv.getByUserId(userId, from, size);
    }

    @PostMapping
    public EventFullDto create(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @RequestBody @Validated NewEventDto newEventDto) {
        log.info("Добавление нового события {}", newEventDto);
        return eventServicePriv.create(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getFullInfoByEventIdAndUserId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId) {
        log.info("Получение полной информации о событии с id: {} добавленном пользователем " +
                "с id: {}", eventId, userId);
        return eventServicePriv.getFullInfoByEventIdAndUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId,
            @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Изменение события {} с id: {} добавленного пользователем с id: {}",
                updateEventAdminRequest, eventId, userId);
        return eventServicePriv.updateByUserIdAndEventId(userId, eventId, updateEventAdminRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId) {
        log.info("Получение информации о запросах на участие в событии с id: {} ползователя с id: {}",
                eventId, userId);
        return eventServicePriv.getRequestByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public List<EventRequestStatusUpdateResult> updateStatusRequestByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId,
            @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("Изменение статуса заявок на участие в событии с id: {} пользователя с id: {} ", eventId, userId);
        return eventServicePriv.updateStatusRequestByUserIdAndEventId(userId, eventId, statusUpdateRequest);
    }
}
