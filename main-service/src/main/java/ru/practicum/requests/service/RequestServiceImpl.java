package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.state.State;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Transactional
    public ParticipationRequestDto addRequest(Long eventId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
        if (event.getInitiator().equals(user)) {
            throw new DataConflictException("Request couldn't be created by initiator");
        }

        Optional<ParticipationRequest> request = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (request.isPresent()) {
            throw new DataConflictException("Request already exists");
        }
        if (!event.getState().equals((State.PUBLISHED))) {
            throw new DataConflictException("Couldn't created request for not published event");
        }
        List<ParticipationRequest> requests = findConfirmedRequests(List.of(event));
        if (requests.size() == event.getParticipantLimit()) {
            throw new DataConflictException("Reached event's limit of requests");
        }

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .build();

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
        } else {
            participationRequest.setStatus(RequestStatus.PENDING);
        }

        return RequestMapper.toParticipationRequestDto(requestRepository.save(participationRequest));
    }

    public List<ParticipationRequest> findConfirmedRequests(List<Event> events) {
        return requestRepository.findAllByEventInAndStatusIs(events, RequestStatus.CONFIRMED);
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(Long requestId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + " not found"));
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(ParticipationRequest.class.getSimpleName() + " not found"));
        if (!user.equals(request.getRequester())) {
            throw new DataConflictException("Only requester can cancel request");
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> allUserRequests(Long userId) {
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequest(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + " not found"));
        if (!user.equals(event.getInitiator())) {
            throw new DataConflictException("User isn't initiator");
        }
        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult update(Long eventId, Long userId, EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + " not found"));
        if (!user.equals(event.getInitiator())) {
            throw new DataConflictException("User isn't initiator");
        }
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            throw new DataConflictException("There is no more confirms for this event");
        }
        int confirmedRequests = findConfirmedRequests(List.of(event)).size();
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(request.getRequestIds());
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> requestDtos;
        switch (request.getStatus()) {
            case CONFIRMED:
                for (ParticipationRequest r : requests) {
                    if (r.getStatus().equals(RequestStatus.PENDING)) {
                        throw new DataConflictException("Status must be equals pending");
                    }
                    if (confirmedRequests == event.getParticipantLimit()) {
                        throw new DataConflictException("Reached participant limit");
                    }
                    r.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests++;
                }
                requestDtos = requestRepository.saveAll(requests).stream()
                        .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
                updateResult.setConfirmedRequests(requestDtos);
                break;
            case REJECTED:
                for (ParticipationRequest r : requests) {
                    if (r.getStatus().equals(RequestStatus.PENDING)) {
                        throw new DataConflictException("Status must be equals pending");
                    }
                    r.setStatus(RequestStatus.REJECTED);
                }
                requestDtos = requestRepository.saveAll(requests).stream()
                        .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
                updateResult.setRejectedRequests(requestDtos);
                break;
        }
        return updateResult;
    }

}