package ru.practicum.requests.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private ParticipationRequestDto confirmedRequests;
    private ParticipationRequestDto rejectedRequests;
}

