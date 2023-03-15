package ru.practicum.events.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.service.EventServicePrivate;
import ru.practicum.events.state.State;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.util.EwmObjectFinder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServicePrivateImpl implements EventServicePrivate {

    private final EwmObjectFinder finder;
    private final EventRepository repository;
    private final RequestRepository requestRepository;
    private static final Long HOURS_FROM_NOW = 2L;

    @Override
    @Transactional
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        from = from.equals(size) ? from / size : from;
        final PageRequest pageRequest = PageRequest.of(from, size, Sort.by(DESC, "id"));
        List<Event> events = repository.findAllByInitiatorId(userId, pageRequest).getContent();
        List<EventShortDto> eventShorts = events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        log.info("Получены события: {} добавленные пользователем с id: {}", eventShorts, userId);
        return eventShorts;
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        checkEventDate(newEventDto.getEventDate());
        User initiator = finder.findUser(userId);
        Category category = finder.findCategory(newEventDto.getCategory());
        EventFullDto event =
                EventMapper.toEventFullDto(repository.save(EventMapper.toEvent(newEventDto, initiator, category)));
        log.info("Создано новое событие - " + event.getTitle());
        return event;
    }

    @Override
    public EventFullDto getEventByIdAndUserId(Long userId, Long eventId) {
        Event event = findByEventIdAndUserId(eventId, userId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByIdAndUserId(
            Long userId, Long eventId, UpdateEventUserRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getEventDate() != null) {
            checkEventDate(updateEventAdminRequest.getEventDate());
        }
        Event event = findByEventIdAndUserId(eventId, userId);
        if (event.getState() == State.PUBLISHED) {
            throw new DataConflictException("Нельзя уже опубликованное событие");
        }
        isChangeable(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(EventMapper.updateEventUser(
                event, updateEventAdminRequest, null));
        log.info("Обновлено событие - " + eventFullDto.getTitle());
        return eventFullDto;
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId) {
        return getRequests(userId, eventId);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequestByUserIdAndEventId(
            Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        User user = finder.findUser(userId);
        Event event = finder.findEvent(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new ValidationException("Принять заявки может только создатель события");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new DataConflictException("Достигнут лимит заявок на участие");
        }

        List<ParticipationRequest> requests = updateRequest.getRequestIds()
                .stream()
                .map(finder::findRequest)
                .collect(Collectors.toList());

        switch (updateRequest.getStatus()) {
            case CONFIRMED:
                return getStatusUpdateConfirmedStatus(requests, event);
            case REJECTED:
                return getStatusUpdateRejectedStatus(requests);
            default:
                throw new ValidationException("Некорректный статус");
        }

    }

    private EventRequestStatusUpdateResult getStatusUpdateConfirmedStatus(List<ParticipationRequest> requests, Event event) {
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new DataConflictException("Статус заявки должен быть PENDING");
            }
            if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                request.setStatus(RequestStatus.REJECTED);
                rejected.add(RequestMapper.toParticipationRequestDto(request));
            } else {
                request.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                confirmed.add(RequestMapper.toParticipationRequestDto(request));
            }
            requestRepository.save(request);
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }

    private EventRequestStatusUpdateResult getStatusUpdateRejectedStatus(List<ParticipationRequest> requests) {
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new DataConflictException("Статус заявки должен быть PENDING");
            }
            request.setStatus(RequestStatus.REJECTED);
            requestRepository.save(request);
            rejected.add(RequestMapper.toParticipationRequestDto(request));
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(rejected)
                .build();
    }

    private Event findByEventIdAndUserId(Long eventId, Long userId) {
        Event event = repository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException(
                String.format("Событие с id - " + eventId + " не найдено")));
        log.info("Найдено событие с id - " + eventId);
        return event;
    }

    private List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        User user = finder.findUser(userId);
        Event event = finder.findEvent(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new ValidationException("Только инициатор может получать запросы на события");
        }

        return requestRepository.findByEvent(event)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private void isChangeable(Event event) {
        boolean isChangeable = event.getState() == State.CANCELED ||
                event.getRequestModeration() || event.getState() == State.PENDING;
        if (!isChangeable)
            throw new ValidationException(
                    "Изменить можно только отмененные события или события в состоянии ожидания модерации");
    }

    private void checkEventDate(LocalDateTime eventDateTime) {
        boolean isValidDate = !eventDateTime.isBefore(LocalDateTime.now().plusHours(HOURS_FROM_NOW));
        if (!isValidDate)
            throw new ValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через " + HOURS_FROM_NOW + " час(ов) от текущего момента");
    }

}