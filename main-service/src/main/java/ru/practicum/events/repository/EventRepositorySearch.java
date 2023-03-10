package ru.practicum.events.repository;

import ru.practicum.events.model.Event;
import ru.practicum.events.state.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositorySearch {

    List<Event> publicSearch(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, long from, int size);

    List<Event> adminSearch(List<Long> users, List<State> states, List<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, long from, int size);
}
