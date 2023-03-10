package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.service.EventServicePrivate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventServicePrivate eventPrivateService;

    @PostMapping
    public ResponseEntity<Object> addEvent(@PathVariable String userId, @RequestBody @NotNull @Valid NewEventDto newEventDto) {
        return new ResponseEntity<>(eventPrivateService.addEvent(Long.parseLong(userId), newEventDto), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(eventPrivateService.getUserEvent(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @RequestBody @NotNull @Valid UpdateEventUserRequest updateEventRequest) {
        return new ResponseEntity<>(eventPrivateService.updateEvent(userId, eventId, updateEventRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getUserEvents(@PathVariable Long userId,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero long from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return new ResponseEntity<>(eventPrivateService.getUserEvents(userId, from, size), HttpStatus.OK);
    }
}
