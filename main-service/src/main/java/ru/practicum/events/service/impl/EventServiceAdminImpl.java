package ru.practicum.events.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventAdminSearch;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.service.EventServiceAdmin;
import ru.practicum.events.service.EventServiceUtil;
import ru.practicum.events.state.State;
import ru.practicum.exception.DataConflictException;
import ru.practicum.util.EwmObjectFinder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceAdminImpl implements EventServiceAdmin {
    private final EventRepository repository;
    private final EventServiceUtil utilService;
    private static final Long TIME_DIFFERENCE = 1L;
    private final EwmObjectFinder finder;

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest request) {
        Event event = finder.findEvent(eventId);
        if (request.getEventDate() != null) {
            checkDate(request.getEventDate(), event.getPublishedOn());
        }
        if (event.getState() != State.PENDING) {
            throw new DataConflictException("Нельзя изменить событие. Статус события должен быть PENDING");
        }
        Category category = request.getCategory() == null ? null : finder.findCategory(request.getCategory());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(repository.save(
                EventMapper.updateEventAdmin(event, request, category)));
        log.info("Обновлено событие " + eventFullDto.getTitle());
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsWithFilters(EventAdminSearch e) {
        List<Event> events = repository.adminSearch(e.getUsers(), e.getStates(), e.getCategories(),
                e.getRangeStart(), e.getRangeEnd(), e.getFrom(), e.getSize());
        return addViewsFromStatistic(events);
    }

    private void checkDate(LocalDateTime checked, LocalDateTime original) {
        if (checked.isBefore(original.minusHours(TIME_DIFFERENCE))) {
            throw new DataConflictException("Нельзя изменить событие. Дата начала изменяемого события должна быть не " +
                    "ранее чем за " + TIME_DIFFERENCE + " час(ов) от даты публикации");
        }
    }

    private List<EventFullDto> addViewsFromStatistic(List<Event> events) {
        List<EventFullDto> eventDtoList = events.stream()
                .map(EventMapper::toEventFullDto).collect(Collectors.toList());

        Map<Long, Long> views = utilService.getStats(events, false);
        if (!views.isEmpty()) {
            eventDtoList.forEach(e -> e.setViews(views.get(e.getId())));
        }
        return eventDtoList;
    }
}
