package ru.practicum.requests.mapper;

import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest source) {
        return ParticipationRequestDto.builder()
                .id(source.getId())
                .created(source.getCreated())
                .eventId(source.getEvent().getId())
                .requesterId(source.getRequester().getId())
                .status(source.getStatus())
                .build();
    }

    public static EventRequestStatusUpdateResult toRejectedRequest(List<ParticipationRequestDto> source){
        return EventRequestStatusUpdateResult.builder()
                .rejectedRequests(source)
                .build();
    }

    public static EventRequestStatusUpdateResult toConfirmedRequest(List<ParticipationRequestDto> source){
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(source)
                .build();
    }

}
