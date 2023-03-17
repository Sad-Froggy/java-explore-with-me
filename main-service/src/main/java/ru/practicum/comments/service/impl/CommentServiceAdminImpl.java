package ru.practicum.comments.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.comments.service.CommentServiceAdmin;
import ru.practicum.exception.NotFoundException;
import ru.practicum.util.EwmObjectFinder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceAdminImpl implements CommentServiceAdmin {

    private final CommentRepository commentRepository;

    private final EwmObjectFinder finder;

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        finder.checkUserExistenceById(userId);
        return commentRepository.findByCommentatorId(userId, pageable).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        return CommentMapper.toCommentDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id " + commentId + " не найден")));
    }

    @Override
    public void deleteCommentById(Long commentId) {
        try {
            commentRepository.deleteById(commentId);
            log.info("Комментарий с id " + commentId + " удален");
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Комментарий с id " + commentId + " не найден");
        }
    }

    @Override
    public void deleteCommentsByUser(Long userId) {
        finder.checkUserExistenceById(userId);
        log.info("Все комментарии пользователя удалены");
        commentRepository.deleteByCommentatorId(userId);
    }

    @Override
    public void deleteCommentsByEvent(Long eventId) {
        finder.checkEventExistenceById(eventId);
        log.info("Все комментарии события удалены");
        commentRepository.deleteCommentsByEventId(eventId);
    }

}
