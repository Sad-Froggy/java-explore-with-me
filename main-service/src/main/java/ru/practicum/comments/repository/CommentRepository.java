package ru.practicum.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByEventId(Long eventId, Pageable pageable);

    List<Comment> findByCommentatorId(Long commentatorId, Pageable pageable);

    List<Comment> findByReplyingTo(Long commentId, Pageable pageable);

    void deleteByCommentatorId(Long commentatorId);

    void deleteCommentsByEventId(Long eventId);

}
