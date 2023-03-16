package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventPublicSearch;
import ru.practicum.events.model.EventSort;
import ru.practicum.events.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPublic {
    private final EventServicePublic eventPublicService;

    @GetMapping
    public List<EventFullDto> getAllWithFiltering(
            HttpServletRequest request,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Запрос поиска событий по фильтру от пользователя");
        EventPublicSearch eventPublicSearch = EventPublicSearch.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
        return  eventPublicService.getEventsWithFilters(eventPublicSearch, request.getRemoteAddr());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(
            HttpServletRequest request,
            @PathVariable("eventId") @PositiveOrZero Long eventId) {
        log.info("Запрос получения информации о событии с id {} от пользователя", eventId);
        return eventPublicService.getEventById(eventId, request);
    }
}
