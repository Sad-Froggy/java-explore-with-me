package ru.practicum.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getByUserId(
            @PathVariable("userId") @PositiveOrZero Long userId) {
        return requestService.getByUserId(userId);
    }

    @PostMapping
    public ParticipationRequestDto createByUserId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @RequestParam(name = "eventId") @PositiveOrZero Long eventId) {
        return requestService.createByUserId(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancellationByUserIdAndRequestId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("requestId") @PositiveOrZero Long requestId) {
        return requestService.cancellationByUserIdAndRequestId(userId, requestId);
    }
}
