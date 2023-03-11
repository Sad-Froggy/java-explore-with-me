package ru.practicum.requests.mapper;

import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setCreated(request.getCreated());
        participationRequestDto.setEventId(request.getEvent().getId());
        participationRequestDto.setId(request.getId());
        participationRequestDto.setRequesterId(request.getRequester().getId());
        participationRequestDto.setStatus(request.getStatus());
        return participationRequestDto;
    }

    public static ParticipationRequest makeRequest(User requester, Event event, Boolean checkModeration) {
        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(requester);
        request.setEvent(event);
        request.setStatus(checkModeration ? RequestStatus.PENDING : RequestStatus.CONFIRMED);
        request.setCreated(LocalDateTime.now());
        event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
        return request;
    }

    public static void cancelRequest(ParticipationRequest request, Event event) {
        request.setStatus(RequestStatus.CANCELED);
        event.setConfirmedRequests(event.getConfirmedRequests() > 0 ? event.getConfirmedRequests() - 1L :
                event.getConfirmedRequests());
    }
}

