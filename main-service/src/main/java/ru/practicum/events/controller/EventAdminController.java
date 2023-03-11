package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.EventServiceAdmin;
import ru.practicum.events.state.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventAdminController {
    private final EventServiceAdmin serviceAdmin;

    @GetMapping
    public List<EventFullDto> getAllByFiltering(
            @RequestParam(name = "users", defaultValue = "") List<Long> users,
            @RequestParam(name = "states", defaultValue = "PENDING") List<String> states,
            @RequestParam(name = "categories", defaultValue = "") List<Long> categories,
            @RequestParam(name = "rangeStart", defaultValue = "") String rangeStart,
            @RequestParam(name = "rangeEnd", defaultValue = "") String rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        List<State> eventStates = checkState(states);
        log.info("Поиск событий по фильтру -> users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}," +
                " from {}, size: {}", users, states, categories, rangeStart, rangeEnd, from, size);
        return serviceAdmin.getAllByFiltering(users, eventStates, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateDataAndStatus(
            @PathVariable("eventId") @PositiveOrZero Long eventId,
            @RequestBody UpdateEventAdminRequest request) {
        log.info("Обновление события {} с id: {}", request, eventId);
        return serviceAdmin.updateDataAndStatus(eventId, request);
    }

    private List<State> checkState(List<String> stringStates) {
        List<State> eventStates = new ArrayList<>();
        for (String stringEventState : stringStates) {
            State eventState = State.valueOf(stringEventState);
            eventStates.add(eventState);
        }
        return eventStates;
    }
}