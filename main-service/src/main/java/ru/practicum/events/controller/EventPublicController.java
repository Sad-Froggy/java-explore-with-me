package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.EventSort;
import ru.practicum.events.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventPublicController {
    private final EventServicePublic eventServicePub;

    @GetMapping
    public List<EventShortDto> getAllWithFiltering(
            HttpServletRequest request,
            @RequestParam(name = "text", defaultValue = "") String text,
            @RequestParam(name = "categories", defaultValue = "") List<Long> categories,
            @RequestParam(name = "paid", defaultValue = "false") Boolean paid,
            @RequestParam(name = "rangeStart", defaultValue = "") String rangeStart,
            @RequestParam(name = "rangeEnd", defaultValue = "") String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "true") Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        EventSort eventSort = EventSort.from(sort)
                .orElseThrow(() -> new ValidationException("Неверный параметр сортировки: " + sort));
        log.info("Получение событий по фильтру text: {}, categories: {}, paid: {}, rangeStart: {}," +
                        " rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}", text, categories,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventServicePub.getAllWithFiltering(
                request, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, eventSort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(
            HttpServletRequest request,
            @PathVariable("eventId") @PositiveOrZero Long eventId) {
        log.info("Получение подробной информации об опубликованном событии по id: {}", eventId);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventServicePub.getById(request, eventId);
    }
}
