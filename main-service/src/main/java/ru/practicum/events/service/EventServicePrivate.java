package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.state.State;
import ru.practicum.events.state.StateAction;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.service.RequestService;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.events.mapper.EventMapper.eventToEventFullDto;
import static ru.practicum.events.mapper.EventMapper.newEventDtoToEvent;


@Service
@RequiredArgsConstructor
public class EventServicePrivate {
    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final EventService eventCommonService;

    private final RequestService requestService;


    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataConflictException("Event date should be in the future");
        }
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(Category.class.getSimpleName() + " not found"));
        Event event = newEventDtoToEvent(newEventDto, category);
        event.setCategory(category);
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + " not found"));
        event.setInitiator(initiator);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        return eventToEventFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<EventFullDto> getUserEvents(Long userId, long from, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Event> events = eventRepository
                .findAllByIdIsGreaterThanEqualAndInitiatorIdIs(from, userId, pageRequest);
        List<EventFullDto> eventFullDtos = eventCommonService.setViewsToEvents(events);
        List<ParticipationRequest> confirmedRequests = requestService.findConfirmedRequests(events);
        for (EventFullDto fullDto : eventFullDtos) {
            fullDto.setConfirmedRequests((int) confirmedRequests.stream()
                    .filter(r -> r.getEvent().getId().equals(fullDto.getId()))
                    .count());
        }
        return eventFullDtos;
    }

    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException("Initiator and user have different ids");
        }
        EventFullDto eventFullDto = eventToEventFullDto(event);
        if (eventFullDto.getState().equals(State.PUBLISHED)) {
            Map<Long, Long> views = eventCommonService.getStats(List.of(event), false);
            eventFullDto.setViews(views.get(event.getId()));
            List<ParticipationRequest> confirmedRequests = requestService
                    .findConfirmedRequests(List.of(event));
            eventFullDto.setConfirmedRequests(confirmedRequests.size());
        }
        return eventFullDto;
    }

    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException("Only initiator can update event");
        }
        Event updatedEvent = patchEvent(event, updateEventUserRequest);
        switch (updateEventUserRequest.getStateAction()) {
            case SEND_TO_REVIEW:
                updatedEvent.setState(State.PENDING);
                break;
            case CANCEL_REVIEW:
                updatedEvent.setState(State.CANCELED);
                break;
        }
        return eventToEventFullDto(eventRepository.save(updatedEvent));
    }

    private Event patchEvent(Event event, UpdateEventUserRequest updatedEvent) {
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
