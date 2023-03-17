package ru.practicum.comments.model;

import lombok.*;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000, nullable = false)
    private String content;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User commentator;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime created;

    @Column(name = "replying_id")
    private Long replyingTo;

    @Column(name = "edited_on")
    private LocalDateTime editedOn;

    @Column(name = "edited")
    private boolean isEdited;

}
