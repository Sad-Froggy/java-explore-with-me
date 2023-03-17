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

    @GetMapping("/{id}")
    public CommentDto getById(@PathVariable Long id) {
        log.info("Запрос комментария по id " + id);
        return commentServiceAdmin.getCommentById(id);
    }

    @GetMapping("/user/{userId}")
    public List<CommentDto> getByUserId(@PathVariable Long userId,
                                      @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Запрос комментария по id пользователя - " + userId);
        return commentServiceAdmin.getCommentsByUserId(userId, from, size);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long id) {
        log.info("Запрос удаления комментария по id - " + id + " от администратора");
        commentServiceAdmin.deleteCommentById(id);
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
