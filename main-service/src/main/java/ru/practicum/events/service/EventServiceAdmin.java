package ru.practicum.events.service;

import ru.practicum.events.dto.EventAdminSearch;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;

import java.util.List;

public interface EventServiceAdmin {
    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest request);

    List<EventFullDto> getEventsWithFilters(EventAdminSearch e);
}
