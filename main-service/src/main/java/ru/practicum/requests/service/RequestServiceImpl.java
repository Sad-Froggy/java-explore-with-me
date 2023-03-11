package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventServicePublic;
import ru.practicum.events.state.State;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventServicePublic eventServicePub;
    private final UserService userService;

    @Override
    public List<ParticipationRequestDto> getByUserId(Long userId) {
        userService.getByIdForService(userId);
        return findAllByUserId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createByUserId(Long userId, Long eventId) {
        User requester = userService.getByIdForService(userId);
        Event event = eventServicePub.getByIdForService(eventId);
        findByRequesterIdAndEventId(userId, eventId);
        checkValidationRequest(userId, event);
        ParticipationRequestDto requestDto = RequestMapper.toParticipationRequestDto(
                requestRepository.save(RequestMapper.makeRequest(requester, event, checkModeration(event))));
        log.info("Добавлен запрос {}", requestDto);
        return requestDto;
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancellationByUserIdAndRequestId(Long userId, Long requestId) {
        userService.getByIdForService(userId);
        ParticipationRequest request = findById(requestId);
        Event event = eventServicePub.getByIdForService(request.getEvent().getId());
        RequestMapper.cancelRequest(request, event);
        ParticipationRequestDto requestDto = RequestMapper.toParticipationRequestDto(request);
        log.info("Отменен запрос на участие в событии -> {}", requestDto);
        return requestDto;
    }

    private List<ParticipationRequest> findAllByUserId(Long requesterId) {
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(requesterId);
        log.info("Найдены заявки {} пользователя с id: {}", requests, requesterId);
        return requests;
    }

    private ParticipationRequest findById(Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(
                String.format("Заявка с id: %s не найдена", requestId)));
        log.info("Найдена заявка {}", request);
        return request;
    }

    private void findByRequesterIdAndEventId(Long requesterId, Long eventId) {
        Optional<ParticipationRequest> requests = requestRepository.findByEventIdAndRequesterId(eventId, requesterId);
        if (requests.isPresent()) throw new ValidationException("Нельзя добавить повторный запрос");
    }

    private void checkValidationRequest(Long userId, Event event) {
        boolean checkRequest = userId.equals(event.getInitiator().getId()) ||
                !event.getState().equals(State.PUBLISHED) ||
                event.getConfirmedRequests() >= event.getParticipantLimit();
        if (checkRequest) throw new ValidationException("Not valid value for made request");
    }

    private Boolean checkModeration(Event event) {
        return event.getRequestModeration();
    }
}