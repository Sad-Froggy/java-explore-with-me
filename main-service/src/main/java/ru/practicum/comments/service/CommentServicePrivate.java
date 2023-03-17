package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentRequest;

public interface CommentServicePrivate {

    CommentDto createComment(NewCommentDto commentDto, Long userId, Long eventId, Long replyingTo);

    CommentDto editComment(Long commentId, UpdateCommentRequest request, Long userId, Long eventId);

    void deleteCommentUser(Long commentId, Long userId);

}
