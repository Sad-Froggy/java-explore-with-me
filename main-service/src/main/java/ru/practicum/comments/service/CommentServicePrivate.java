package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentDtoRequest;

public interface CommentServicePrivate {

    CommentDto createComment(CommentDtoRequest commentDto, Long userId, Long eventId, Long replyingTo);

    CommentDto editComment(Long commentId, CommentDtoRequest request, Long userId, Long eventId);

    void deleteCommentUser(Long commentId, Long userId);

}
