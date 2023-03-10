package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.EventServiceAdmin;
import ru.practicum.events.state.State;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
@RequiredArgsConstructor
public class EventAdminController {
    private final EventServiceAdmin eventAdminService;

    @GetMapping
    public ResponseEntity<Object> findEvents(List<Long> users,
                                             List<State> states,
                                             List<Long> categories,
                                             LocalDateTime start,
                                             LocalDateTime end) {
        return new ResponseEntity<>(eventAdminService
                .findByAdmin(users, states, categories, start, end), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable Long eventId,
                                              @RequestBody @NotNull @Valid UpdateEventAdminRequest updateEventRequest) {
        return new ResponseEntity<>(eventAdminService.adminUpdateEvent(eventId, updateEventRequest), HttpStatus.OK);
    }
}
