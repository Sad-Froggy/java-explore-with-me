package ru.practicum.events.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final StatsClient statsClient;

    public Map<Long, Long> getStats(List<Event> events, Boolean unique) {
        Map<Long, Long> result = new HashMap<>();
        Optional<LocalDateTime> start = events.stream().map(Event::getPublishedOn).min(LocalDateTime::compareTo);
        if (start.isEmpty()) {
            return new HashMap<>();
        }
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        List<String> uris = eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList());

        ResponseEntity<List<ViewStatsDto>> response = statsClient.get(start.get(), LocalDateTime.now(), uris, unique);
        List<ViewStatsDto> stats;
        ObjectMapper mapper = new ObjectMapper();
        try {
            stats = Arrays.asList(mapper.readValue(mapper.writeValueAsString(response.getBody()), ViewStatsDto[].class));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        for (Long eventId : eventIds) {
            Long views = 0L;
            Optional<Long> stat = stats.stream()
                    .filter(s -> s.getUri().equals("/events/" + eventId)).map(ViewStatsDto::getHits).findFirst();
            if (stat.isPresent()) {
                views = stat.get();
            }
            result.put(eventId, views);
        }

        return result;
    }

    public List<EventFullDto> setViewsToEvents(List<Event> events) {
        List<EventFullDto> eventFullDtos = events.stream()
                .map(EventMapper::eventToEventFullDto).collect(Collectors.toList());

        Map<Long, Long> views = getStats(events, false);
        if (!views.isEmpty()) {
            eventFullDtos.forEach(e -> e.setViews(views.get(e.getId())));
        }
        return eventFullDtos;
    }
}
