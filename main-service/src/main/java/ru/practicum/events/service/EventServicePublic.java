package ru.practicum.events.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventPublicSearch;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePublic {
    @Transactional
    List<EventFullDto> getEventsWithFilters(EventPublicSearch ev, String ip);

    @Transactional(readOnly = true)
    EventFullDto getEventById(Long eventId, HttpServletRequest request);
}
