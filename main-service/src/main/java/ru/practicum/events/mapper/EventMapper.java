package ru.practicum.events.mapper;

import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.state.State;
import ru.practicum.events.state.StateAction;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category) {
        LocalDateTime currentTime = LocalDateTime.now();
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .description(newEventDto.getDescription())
                .createdOn(currentTime)
                .eventDate(newEventDto.getEventDate())
                .initiator(initiator)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(currentTime)
                .requestModeration(newEventDto.getRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .eventDate(event.getEventDate())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public static Event updateEventAdmin(Event event, UpdateEventAdminRequest request, Category category) {
        if (request.getAnnotation() != null) event.setAnnotation(request.getAnnotation());
        if (category != null) event.setCategory(category);
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventDate() != null) event.setEventDate(request.getEventDate());
        if (request.getLocation() != null) event.setLocation(request.getLocation());
        if (request.getPaid() != null) event.setPaid(request.getPaid());
        if (request.getParticipantLimit() != null) event.setParticipantLimit(request.getParticipantLimit());
        if (request.getRequestModeration() != null) event.setRequestModeration(request.getRequestModeration());
        if (request.getStateAction() == StateAction.PUBLISH_EVENT) event.setState(State.PUBLISHED);
        if (request.getStateAction() == StateAction.REJECT_EVENT) event.setState(State.CANCELED);
        if (request.getStateAction() == StateAction.CANCEL_REVIEW) event.setState(State.CANCELED);
        if (request.getTitle() != null) event.setTitle(request.getTitle());
        return event;
    }

    public static Event updateEventUser(Event event, UpdateEventUserRequest request, Category category) {
        if (request.getAnnotation() != null) event.setAnnotation(request.getAnnotation());
        if (category != null) event.setCategory(category);
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventDate() != null) event.setEventDate(request.getEventDate());
        if (request.getLocation() != null) event.setLocation(request.getLocation());
        if (request.getPaid() != null) event.setPaid(request.getPaid());
        if (request.getParticipantLimit() != null) event.setParticipantLimit(request.getParticipantLimit());
        if (request.getRequestModeration() != null) event.setRequestModeration(request.getRequestModeration());
        if (request.getStateAction() == StateAction.PUBLISH_EVENT) event.setState(State.PUBLISHED);
        if (request.getStateAction() == StateAction.REJECT_EVENT) event.setState(State.CANCELED);
        if (request.getStateAction() == StateAction.CANCEL_REVIEW) event.setState(State.CANCELED);
        if (request.getStateAction() == StateAction.SEND_TO_REVIEW) event.setState(State.PENDING);
        if (request.getTitle() != null) event.setTitle(request.getTitle());
        return event;
    }
}
