package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentDtoRequest;
import ru.practicum.comments.service.CommentServicePrivate;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/users/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentControllerPrivate {

    private final CommentServicePrivate commentServicePrivate;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid CommentDtoRequest commentDto,
                                    @RequestParam @PositiveOrZero Long userId,
                                    @PathVariable @PositiveOrZero Long eventId,
                                    @RequestParam(required = false) Long replyingTo) {
        log.info("Запрос создания комментария к событию с id - " + eventId + " от пользователя с id - " + userId);
        return commentServicePrivate.createComment(commentDto, userId, eventId, replyingTo);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto editComment(@PathVariable Long commentId,
                                  @RequestBody @Valid CommentDtoRequest request,
                                  @RequestParam Long userId,
                                  @RequestParam Long eventId) {
        log.info("Запрос обновления комментария с id - " + commentId + " от пользователя с id - " + userId +
                " к событию с id - " + eventId);
        return commentServicePrivate.editComment(commentId, request, userId, eventId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId,
                                  @RequestParam Long userId) {
        log.info("Запрос удаления комментария по id - " + commentId + " от пользователя с id - " + userId);
        commentServicePrivate.deleteCommentUser(commentId, userId);
    }

}
