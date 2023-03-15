package ru.practicum.events.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventPublicSearch;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventSort;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.service.EventServicePublic;
import ru.practicum.events.service.EventServiceUtil;
import ru.practicum.events.state.State;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.service.RequestServicePrivate;
import ru.practicum.util.EwmObjectFinder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServicePublicImpl implements EventServicePublic {
    private final StatsClient statsClient;
    private final EventRepository repository;
    private final EventServiceUtil utilService;
    private final EwmObjectFinder finder;
    private final RequestServicePrivate requestServicePrivate;

    @Override
    @Transactional
    public List<EventFullDto> getEventsWithFilters(EventPublicSearch eventSearch, String ip) {
        if (eventSearch == null) {
            return Collections.emptyList();
        }
        List<Event> events = repository.publicSearch(eventSearch.getText(), eventSearch.getCategories(), eventSearch.getPaid(),
                eventSearch.getRangeStart(), eventSearch.getRangeEnd(), eventSearch.getFrom(), eventSearch.getSize());
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        List<EventFullDto> eventDtoList = events.stream()
                .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        Map<Long, Long> views = utilService.getStats(events, false);
        eventDtoList.forEach(e -> e.setViews(views.get(e.getId())));
        List<ParticipationRequest> confirmedRequests = requestServicePrivate.findConfirmedRequests(events);
        for (EventFullDto eventFullDto : eventDtoList) {
            eventFullDto.setConfirmedRequests(confirmedRequests.stream()
                    .filter(r -> r.getEvent().getId().equals(eventFullDto.getId())).count());
        }

        statsClient.post("/events", ip, LocalDateTime.now().withNano(0), "ewm-main-service");
        events.forEach(e -> statsClient.post(
                "/events" + e.getId(),
                ip,
                LocalDateTime.now().withNano(0),
                "ewm-main-service"));
        if (eventSearch.getSort() != null && eventSearch.getSort().equals(EventSort.VIEWS)) {
            return eventDtoList.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews)).collect(Collectors.toList());
        }
        return eventDtoList.stream()
                .sorted(Comparator.comparing(EventFullDto::getEventDate)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = finder.findEvent(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException(event + " not found");
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        Map<Long, Long> views = utilService.getStats(List.of(event), false);
        eventFullDto.setViews(views.get(event.getId()));

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        statsClient.post(uri, ip, LocalDateTime.now().withNano(0), "ewm-main-service");
        return eventFullDto;
    }
}