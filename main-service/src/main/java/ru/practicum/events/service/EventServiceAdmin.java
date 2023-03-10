package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.state.State;
import ru.practicum.events.state.StateAction;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.events.mapper.EventMapper.eventToEventFullDto;

@Service
@RequiredArgsConstructor
public class EventServiceAdmin {

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final EventService eventCommonService;

    @Transactional(readOnly = true)
    public List<EventFullDto> findByAdmin(List<Long> users,
                                          List<State> states,
                                          List<Long> categories,
                                          LocalDateTime start,
                                          LocalDateTime end
    ) {
        List<Event> events = eventRepository.adminSearch(users, states, categories, start, end, 0L, 10);
        return eventCommonService.setViewsToEvents(events);
    }

    @Transactional
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest updateEventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
        if (!event.getState().equals(State.PENDING)) {
            throw new DataConflictException("Wrong event state: " + event.getState());
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusSeconds(1))) {
            throw new DataConflictException("Event date should be in the future");
        }
        Event updatedEvent = patchEvent(event, updateEventRequest);
        Event saved = eventRepository.save(updatedEvent);
        return eventToEventFullDto(saved);
    }

    private Event patchEvent(Event event, UpdateEventAdminRequest updatedEvent) {
        Optional.ofNullable(updatedEvent.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updatedEvent.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updatedEvent.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(updatedEvent.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(updatedEvent.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updatedEvent.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updatedEvent.getRequestModeration()).ifPresent(event::setRequestModeration);
        if (updatedEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updatedEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException(Category.class.getSimpleName() + " not found"));
            event.setCategory(category);
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new DataConflictException("Event with state: " + event.getState() + " cannot be changed");
        }

        if (updatedEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            event.setPublishedOn(LocalDateTime.now());
            event.setState(State.PUBLISHED);
        } else if (updatedEvent.getStateAction().equals(StateAction.REJECT_EVENT)) {
            event.setState(State.CANCELED);
        }
        return event;
    }


}
