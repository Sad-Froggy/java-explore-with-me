package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndPointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Sort;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.state.State;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.events.mapper.EventMapper.eventToEventFullDto;

@Service
@RequiredArgsConstructor
public class EventServicePublic {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final EventService eventCommonService;
    private final RequestService requestService;

    @Transactional(readOnly = true)
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(event + " not found");
        }
        EventFullDto eventFullDto = eventToEventFullDto(event);
        Map<Long, Long> views = eventCommonService.getStats(List.of(event), false);
        eventFullDto.setViews(views.get(event.getId()));

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        EndPointHitDto hitDto = EndPointHitDto.builder()
                .app("APP_NAME")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.post(hitDto);
        return eventFullDto;
    }


    @Transactional(readOnly = true)
    public List<EventFullDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, long from, int size, String ip, String sort) {
        List<Event> events = eventRepository.publicSearch(text, categories, paid, rangeStart, rangeEnd, from, size);
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        List<EventFullDto> eventFullDtos = events.stream()
                .map(EventMapper::eventToEventFullDto).collect(Collectors.toList());

        Map<Long, Long> views = eventCommonService.getStats(events, false);

        eventFullDtos.forEach(e -> e.setViews(views.get(e.getId())));

        List<ParticipationRequest> confirmedRequests = requestService.findConfirmedRequests(events);

        for (EventFullDto eventFullDto : eventFullDtos) {
            eventFullDto.setConfirmedRequests((int) confirmedRequests.stream()
                    .filter(r -> r.getEvent().getId().equals(eventFullDto.getId())).count());
        }

        EndPointHitDto hitDto = EndPointHitDto.builder().app("APP_NAME").uri("/events").ip(ip).timestamp(LocalDateTime.now()).build();
        statsClient.post(hitDto);
        events.forEach(e -> {
            hitDto.setUri("/events/" + e.getId());
            statsClient.post(hitDto);
        });
        if (sort != null && Sort.valueOf(sort).equals(Sort.VIEWS)) {
            return eventFullDtos.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews)).collect(Collectors.toList());
        }
        return eventFullDtos.stream()
                .sorted(Comparator.comparing(EventFullDto::getEventDate)).collect(Collectors.toList());
    }
}
