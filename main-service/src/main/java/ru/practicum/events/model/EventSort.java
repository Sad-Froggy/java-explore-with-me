package ru.practicum.events.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum EventSort {
    EVENT_DATE("eventDate"),
    VIEWS("views");
    private final String column;

    public static Optional<EventSort> from(String sort) {
        for (EventSort eventSort : values()) {
            if (eventSort.name().equalsIgnoreCase(sort)) {
                return Optional.of(eventSort);
            }
        }
        return Optional.empty();
    }
}
