package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.CommentServicePublic;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentControllerPublic {

    private final CommentServicePublic commentServicePublic;

    @GetMapping("/event/{eventId}")
    public List<CommentDto> getCommentsByEventId(@PathVariable @PositiveOrZero Long eventId,
                                                 @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Запрос получения комментариев к событию с id - " + eventId);
        return commentServicePublic.getByEventId(eventId, from, size);
    }

    @GetMapping("/replies/{commentId}")
    public List<CommentDto> getCommentReplies(@PathVariable @PositiveOrZero Long commentId,
                                              @PositiveOrZero
                                              @RequestParam(required = false, defaultValue = "0") Integer from,
                                              @Positive
                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Запрос получения ответов на комментарий с id - " + commentId);
        return commentServicePublic.getReplies(commentId, from, size);
    }

}
