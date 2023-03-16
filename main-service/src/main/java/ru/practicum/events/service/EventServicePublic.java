package ru.practicum.events.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventPublicSearch;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePublic {

    List<EventFullDto> getEventsWithFilters(EventPublicSearch ev, String ip);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);
}
