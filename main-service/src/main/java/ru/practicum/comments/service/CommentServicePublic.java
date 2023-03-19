package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface CommentServicePublic {

    List<CommentDto> getByEventId(Long eventId, int from, int size);

    List<CommentDto> getReplies(Long commentId, int from, int size);

}
