package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.state.State;
import ru.practicum.events.state.StateAction;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceAdmin {
    private final EventRepository repository;
    private final EventServicePublic eventServicePub;
    private final CategoryRepository categoryRepository;
    private final EventMapper mapper;
    private static final Long ONE_HOURS = 1L;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Transactional
    public EventFullDto updateDataAndStatus(Long eventId, UpdateEventAdminRequest request) {
        Event event = eventServicePub.getByIdForService(eventId);
        checkDate(event);
        checkStateAction(event, request);
        EventFullDto eventFullDto = mapper.toEventFullDto(
                mapper.toUpdateDataAndStatus(event, request, getCategoryById(request)));
        log.info("Обновлено событие {}", eventFullDto);
        return eventFullDto;
    }
    public List<EventFullDto> getAllByFiltering(List<Long> users, List<State> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, Integer from, Integer size) {
        from = from.equals(size) ? from / size : from;
        final PageRequest pageRequest = PageRequest.of(from, size, Sort.by(DESC, "id"));
        List<Event> events = rangeStart.isEmpty() || rangeEnd.isEmpty() ?
                repository.findAllByFilteringWithoutDate(users, states, categories, pageRequest).getContent() :
                repository.findAllByFiltering(
                        users, states, categories, LocalDateTime.parse(rangeStart, formatter),
                        LocalDateTime.parse(rangeEnd, formatter), pageRequest).getContent();
        List<EventFullDto> eventFulls =
                events.stream().map(mapper::toEventFullDto).collect(Collectors.toList());
        log.info("Получены события {}", eventFulls);
        return eventFulls;
    }

    private void checkDate(Event event) {
        boolean isNotValidDate = event.getEventDate().isBefore(LocalDateTime.now().plusHours(ONE_HOURS));
        if (isNotValidDate)
            throw new DataConflictException(
                    "Дата изменяемого события может быть не раньше, чем за час от даты публикации");
    }

    private Category getCategoryById(UpdateEventAdminRequest request) {
        return request.getCategory() == null ? null : categoryRepository.findById(request.getCategory()).get();
    }

    private void checkStateAction(Event event, UpdateEventAdminRequest request) {
        boolean isPublish = request.getStateAction().equals(StateAction.PUBLISH_EVENT) ||
                event.getState().equals(State.PENDING);
        boolean isCancel = request.getStateAction().equals(StateAction.REJECT_EVENT) &&
                event.getState().equals(State.PENDING);
        if (isPublish) event.setState(State.PUBLISHED);
        if (isCancel) event.setState(State.CANCELED);
        if (!isPublish && !isCancel) throw new ValidationException(
                "Event because it's not in the right state: %s");
    }












//    private Event patchEvent(Event event, UpdateEventAdminRequest updatedEvent) {
//        Optional.ofNullable(updatedEvent.getAnnotation()).ifPresent(event::setAnnotation);
//        Optional.ofNullable(updatedEvent.getDescription()).ifPresent(event::setDescription);
//        Optional.ofNullable(updatedEvent.getEventDate()).ifPresent(event::setEventDate);
//        Optional.ofNullable(updatedEvent.getLocation()).ifPresent(event::setLocation);
//        Optional.ofNullable(updatedEvent.getPaid()).ifPresent(event::setPaid);
//        Optional.ofNullable(updatedEvent.getParticipantLimit()).ifPresent(event::setParticipantLimit);
//        Optional.ofNullable(updatedEvent.getRequestModeration()).ifPresent(event::setRequestModeration);
//        if (updatedEvent.getCategory() != null) {
//            Category category = categoryRepository.findById(updatedEvent.getCategory())
//                    .orElseThrow(() -> new NotFoundException(Category.class.getSimpleName() + " not found"));
//            event.setCategory(category);
//        }
//        if (event.getState().equals(State.PUBLISHED)) {
//            throw new DataConflictException("Event with state: " + event.getState() + " cannot be changed");
//        }
//
//        if (updatedEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
//            event.setPublishedOn(LocalDateTime.now());
//            event.setState(State.PUBLISHED);
//        } else if (updatedEvent.getStateAction().equals(StateAction.REJECT_EVENT)) {
//            event.setState(State.CANCELED);
//        }
//        return event;
//    }


}
