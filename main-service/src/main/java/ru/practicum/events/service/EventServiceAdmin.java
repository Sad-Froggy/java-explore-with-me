package ru.practicum.events.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventAdminSearch;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;

import java.util.List;

public interface EventServiceAdmin {
    @Transactional
    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest request);

    @Transactional(readOnly = true)
    List<EventFullDto> getEventsWithFilters(EventAdminSearch e);
}
