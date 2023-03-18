package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.CommentServiceAdmin;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentControllerAdmin {

    private final CommentServiceAdmin commentServiceAdmin;

    @GetMapping("/{commentId}")
    public CommentDto getById(@PathVariable Long commentId) {
        log.info("Запрос комментария по id " + commentId);
        return commentServiceAdmin.getCommentById(commentId);
    }

    @GetMapping("/user/{userId}")
    public List<CommentDto> getByUserId(@PathVariable Long userId,
                                        @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                        @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Запрос комментария по id пользователя - " + userId);
        return commentServiceAdmin.getCommentsByUserId(userId, from, size);
    }


    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId) {
        log.info("Запрос удаления комментария по id - " + commentId + " от администратора");
        commentServiceAdmin.deleteCommentById(commentId);
    }

    @DeleteMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentsByUserId(@PathVariable Long userId) {
        log.info("Запрос удаления всех комментариев пользователя по id " + userId);
        commentServiceAdmin.deleteCommentsByUser(userId);
    }

    @DeleteMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentsByEventId(@PathVariable Long eventId) {
        log.info("Запрос удаления всех комментариев события по id " + eventId);
        commentServiceAdmin.deleteCommentsByEvent(eventId);
    }

}
