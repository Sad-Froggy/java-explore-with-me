package ru.practicum.comments.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentDtoRequest;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.comments.service.CommentServicePrivate;
import ru.practicum.events.model.Event;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.users.model.User;
import ru.practicum.util.EwmObjectFinder;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServicePrivateImpl implements CommentServicePrivate {

    private final CommentRepository commentRepository;

    private final EwmObjectFinder finder;

    @Override
    public CommentDto createComment(CommentDtoRequest commentDto, Long userId, Long eventId, Long replyingTo) {
        if (replyingTo != null) {
            finder.checkCommentExistenceById(replyingTo);
        }
        User user = finder.findUser(userId);
        Event event = finder.findEvent(eventId);
        if (event.getDisableComments()) {
            throw new ValidationException("К данному событию запрещено оставлять комментарии");
        }
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, user, eventId, replyingTo)));
    }

    @Override
    public CommentDto editComment(Long commentId, CommentDtoRequest request, Long userId, Long eventId) {
        Comment comment = finder.findComment(commentId);
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new DataConflictException("Редактировать можно только свои комментарии");
        }
        if (!comment.getEventId().equals(eventId)) {
            throw new DataConflictException("У данного события нет такого комментария");
        }
        log.info("Обновлён комментарий с id - " + commentId);
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.updateCommentUser(comment, request)));
    }

    @Override
    public void deleteCommentUser(Long commentId, Long userId) {
        Comment comment = finder.findComment(commentId);
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new DataConflictException("Удалить можно только свои комментарии");
        }
        try {
            commentRepository.deleteById(commentId);
            log.info("Комментарий удалён");
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Комментарий с id " + commentId + " не найден");
        }
    }
}
