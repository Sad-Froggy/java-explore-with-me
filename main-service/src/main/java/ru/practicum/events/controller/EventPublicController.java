package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Validated
@RequiredArgsConstructor
public class EventPublicController {
    private final EventServicePublic eventServicePublic;

    @GetMapping("{id}")
    public ResponseEntity<Object> getEvent(@PathVariable @Positive Long id, HttpServletRequest request) {
        return new ResponseEntity<>(eventServicePublic.getEvent(id, request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, long from, int size, String sort,
                                         HttpServletRequest request) {
        List<EventFullDto> eventFullDtos =
                eventServicePublic.search(text, categories, paid, rangeStart, rangeEnd, from, size, request.getRemoteAddr(), sort);
        return new ResponseEntity<>(eventFullDtos, HttpStatus.OK);
    }
}
