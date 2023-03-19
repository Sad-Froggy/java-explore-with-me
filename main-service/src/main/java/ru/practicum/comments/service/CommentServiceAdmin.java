package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface CommentServiceAdmin {

    List<CommentDto> getCommentsByUserId(Long userId, int from, int size);

    CommentDto getCommentById(Long commentId);

    void deleteCommentById(Long commentId);

    void deleteCommentsByUser(Long userId);

    void deleteCommentsByEvent(Long eventId);
}
