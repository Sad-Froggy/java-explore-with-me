package ru.practicum.comments.mapper;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentRequest;
import ru.practicum.comments.model.Comment;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment source) {
        return CommentDto.builder()
                .id(source.getId())
                .content(source.getContent())
                .eventId(source.getEventId())
                .commentator(UserMapper.toUserShortDto(source.getCommentator()))
                .created(source.getCreated())
                .replyingTo(source.getReplyingTo())
                .editedOn(source.getEditedOn())
                .isEdited(source.isEdited())
                .build();
    }

    public static Comment toComment(NewCommentDto source, User user, Long eventId, Long replyingTo) {
        return Comment.builder()
                .content(source.getContent())
                .eventId(eventId)
                .commentator(user)
                .replyingTo(replyingTo)
                .created(LocalDateTime.now())
                .build();
    }

    public static Comment updateCommentUser(Comment comment, UpdateCommentRequest request) {
        comment.setContent(request.getContent());
        comment.setEdited(true);
        comment.setEditedOn(LocalDateTime.now());
        return comment;
    }
}
