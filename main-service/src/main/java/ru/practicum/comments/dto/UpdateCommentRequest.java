package ru.practicum.comments.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {

    @NotBlank(message = "содержание комментария не должно быть пустым")
    private String content;

}
