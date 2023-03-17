package ru.practicum.comments.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.comments.service.CommentServicePublic;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServicePublicImpl implements CommentServicePublic {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDto> getByEventId(Long eventId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        List<Comment> comments = commentRepository.findByEventId(eventId, pageable);
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getReplies(Long commentId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        List<Comment> comments = commentRepository.findByReplyingTo(commentId, pageable);
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
