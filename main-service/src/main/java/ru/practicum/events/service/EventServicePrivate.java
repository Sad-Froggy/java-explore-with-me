package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.service.CategoryServicePublic;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.state.State;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServicePrivate {
    private final EventRepository repository;
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final CategoryServicePublic categoryService;
    private final EventMapper mapper;
    private static final Long TWO_HOURS = 2L;
    private static final Long NONE = 0L;

    public List<EventShortDto> getByUserId(Long userId, Integer from, Integer size) {
        from = from.equals(size) ? from / size : from;
        final PageRequest pageRequest = PageRequest.of(from, size, Sort.by(DESC, "id"));
        List<Event> events = repository.findAllByInitiatorId(userId, pageRequest).getContent();
        List<EventShortDto> eventShorts = events.stream().map(mapper::toEventShortDto).collect(Collectors.toList());
        log.info("Получены события: {} добавленные пользователем с id: {}", eventShorts, userId);
        return eventShorts;
    }

    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User initiator = userService.getByIdForService(userId);
        Category category = categoryService.getByIdForService(newEventDto.getCategory());
        EventFullDto event =
                mapper.toEventFullDto(repository.save(mapper.toEvent(newEventDto, initiator, category)));
        log.info("Новое событие -> {} <- добавлено", event);
        return event;
    }

    public EventFullDto getFullInfoByEventIdAndUserId(Long userId, Long eventId) {
        Event event = findByEventIdAndUserId(eventId, userId);
        EventFullDto eventFullDto = mapper.toEventFullDto(event);
        log.info("Получена полная информация о событии -> {}", eventFullDto);
        return eventFullDto;
    }

    @Transactional
    public EventFullDto updateByUserIdAndEventId(
            Long userId, Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findByEventIdAndUserId(eventId, userId);
        isChangeEvent(event);
        checkDate(event);
        EventFullDto eventFullDto = mapper.toEventFullDto(mapper.toUpdateDataAndStatus(
                event, updateEventAdminRequest, null));
        log.info("Событие изменено {}", eventFullDto);
        return eventFullDto;
    }

    public List<ParticipationRequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId) {
        return getRequest(userId, eventId);
    }

    @Transactional
    public List<EventRequestStatusUpdateResult> updateStatusRequestByUserIdAndEventId(
            Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = findByEventIdAndUserId(eventId, userId);
        List<ParticipationRequestDto> participationRequests = getRequest(userId, eventId);
        if (!checkLimitEvent(event) || !participationRequests.isEmpty()) {

            participationRequests.listIterator();

        }
        List<EventRequestStatusUpdateResult> updateResult = new ArrayList<>();
        participationRequests.forEach(participationRequest -> updateResult
                .add(mapper.toUpdateResult(participationRequest, null)));
        log.info("Изменен статус заявок на участие в событии -> {}", updateResult);
        return updateResult;
    }

    private Event findByEventIdAndUserId(Long eventId, Long userId) {
        Event event = repository.findByIdAndInitiatorId(userId, eventId).orElseThrow(() -> new NotFoundException(
                String.format("Событие с id: %s не найдено", eventId)));
        log.info("Найдено событие с id: {}", eventId);
        return event;
    }

    private void isChangeEvent(Event event) {
        boolean isChange = event.getState().equals(State.CANCELED) ||
                event.getRequestModeration() || event.getState().equals(State.PENDING);
        if (!isChange)
            throw new ValidationException(
                    "Изменить можно только отмененные события или события в состоянии ожидания модерации");
    }

    private void checkDate(Event event) {
        boolean isNotValidDate = event.getEventDate().isBefore(LocalDateTime.now().plusHours(TWO_HOURS));
        if (isNotValidDate)
            throw new ValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента");
    }

    private boolean checkLimitEvent(Event event) {
        boolean isApprove = event.getParticipantLimit().equals(NONE) ||
                !event.getRequestModeration();
        if (isApprove) log.info("Подтверждение заявки не требуется");
        return isApprove;
    }

    private List<ParticipationRequestDto> getRequest(Long userId, Long eventId) {
        Optional<ParticipationRequest> requests = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        List<ParticipationRequestDto> participationRequests = requests.isEmpty() ? Collections.emptyList() :
                requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
        log.info("Получена информации о запросах {} на участие в событии с id: {} пользователя с id: {}",
                participationRequests, eventId, userId);
        return participationRequests;
    }
}