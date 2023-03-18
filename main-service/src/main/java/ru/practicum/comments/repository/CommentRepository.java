package ru.practicum.comments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByEventId(Long eventId, Pageable pageable);

    Page<Comment> findByCommentatorId(Long commentatorId, Pageable pageable);

    Page<Comment> findByReplyingTo(Long commentId, Pageable pageable);

    void deleteByCommentatorId(Long commentatorId);

    void deleteCommentsByEventId(Long eventId);

}
