package ru.practicum.requests.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.state.State;
import ru.practicum.exception.DataConflictException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.requests.service.RequestServicePrivate;
import ru.practicum.users.model.User;
import ru.practicum.util.EwmObjectFinder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServicePrivateImpl implements RequestServicePrivate {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final EwmObjectFinder finder;

    @Override
    @Transactional
    public List<ParticipationRequestDto> getRequestByUserId(Long userId) {
        finder.findUser(userId);
        return findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequestByUserId(Long userId, Long eventId) {
        User requester = finder.findUser(userId);
        Event event = finder.findEvent(eventId);

        if (event.getInitiator().equals(requester)) {
            throw new DataConflictException("Пользователь, создавший событие не может создать запрос на участие в нём");
        }
        if (requestRepository.existsRequestByEventAndRequester(event, requester)) {
            throw new DataConflictException("Запрос на участие в этом событии уже создан");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new DataConflictException("Событие не опубликовано");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new DataConflictException("Достигнут лимит участников");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));

    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequestByIdAndUserId(Long userId, Long requestId) {
        finder.findUser(userId);
        ParticipationRequest request = finder.findRequest(requestId);
        Event event = finder.findEvent(request.getEvent().getId());
        request.setStatus(RequestStatus.CANCELED);
        event.setConfirmedRequests(event.getConfirmedRequests() > 0 ? event.getConfirmedRequests() - 1L :
                event.getConfirmedRequests());
        ParticipationRequestDto requestDto = RequestMapper.toParticipationRequestDto(request);
        log.info("Запрос на участие в событии с id - {} отменён", requestDto);
        return requestDto;
    }

    @Override
    public List<ParticipationRequest> findConfirmedRequests(List<Event> events) {
        List<ParticipationRequest> result = requestRepository.findAllByEventIn(events);
        return result.stream().filter(r -> r.getStatus().equals(RequestStatus.CONFIRMED)).collect(Collectors.toList());
    }

    private List<ParticipationRequest> findAllByRequesterId(Long requesterId) {
        return requestRepository.findAllByRequesterId(requesterId);
    }
}