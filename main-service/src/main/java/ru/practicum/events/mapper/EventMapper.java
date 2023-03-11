package ru.practicum.events.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.state.State;
import ru.practicum.events.state.StateAction;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class EventMapper {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Long EMPTY_VALUE = 0L;
    private static final String CURRENT_DATE_TIME = LocalDateTime.now().format(formatter);

    public Event toEvent(NewEventDto newEventDto, User initiator, Category category) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setConfirmedRequests(EMPTY_VALUE);
        event.setDescription(newEventDto.getDescription());
        event.setCreatedOn(LocalDateTime.parse(CURRENT_DATE_TIME, formatter));
        event.setEventDate(newEventDto.getEventDate());
        event.setInitiator(initiator);
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setPublishedOn(LocalDateTime.parse(CURRENT_DATE_TIME, formatter));
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setState(State.PENDING);
        event.setTitle(newEventDto.getTitle());
        event.setViews(EMPTY_VALUE);
        return event;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event) {
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

    public Event toUpdateDataAndStatus(Event event, UpdateEventAdminRequest request, Category category) {
        if (request.getAnnotation() != null) event.setAnnotation(request.getAnnotation());
        if (category != null) event.setCategory(category);
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventDate() != null) event.setEventDate(request.getEventDate());
        if (request.getLocation() != null) event.setLocation(request.getLocation());
        if (request.getPaid() != null) event.setPaid(request.getPaid());
        if (request.getParticipantLimit() != null) event.setParticipantLimit(request.getParticipantLimit());
        if (request.getRequestModeration() != null) event.setRequestModeration(request.getRequestModeration());
        if (request.getStateAction().equals(StateAction.PUBLISH_EVENT)) event.setState(State.PUBLISHED);
        if (request.getStateAction().equals(StateAction.REJECT_EVENT)) event.setState(State.PENDING);
        if (request.getStateAction().equals(StateAction.CANCEL_REVIEW)) event.setState(State.CANCELED);
        if (request.getTitle() != null) event.setTitle(request.getTitle());
        return event;
    }

    public EventRequestStatusUpdateResult toUpdateResult(ParticipationRequestDto setConfirmedRequests,
                                                         ParticipationRequestDto setRejectedRequests) {
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();
        updateResult.setConfirmedRequests(setConfirmedRequests);
        updateResult.setRejectedRequests(setRejectedRequests);
        return updateResult;
    }
}
