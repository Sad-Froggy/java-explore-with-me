package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventAdminSearch;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.EventServiceAdmin;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerAdmin {
    private final EventServiceAdmin serviceAdmin;

    @GetMapping
    public List<EventFullDto> getAllByFiltering(
            @RequestParam(name = "users", defaultValue = "") List<Long> users,
            @RequestParam(name = "states", defaultValue = "PENDING") List<String> states,
            @RequestParam(name = "categories", defaultValue = "") List<Long> categories,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {

        log.info("Запрос поиска событий по фильтру от администратора");
        EventAdminSearch eventAdminSearch = EventAdminSearch.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        return serviceAdmin.getEventsWithFilters(eventAdminSearch);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateDataAndStatus(
            @PathVariable("eventId") @PositiveOrZero Long eventId,
            @RequestBody UpdateEventAdminRequest request) {
        log.info("Запрос обновления события по id от администратора - " + eventId);
        return serviceAdmin.updateEventAdmin(eventId, request);
    }
}