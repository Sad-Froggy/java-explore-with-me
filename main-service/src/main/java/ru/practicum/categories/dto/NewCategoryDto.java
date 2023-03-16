package ru.practicum.categories.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    private Long id;
    @NotBlank(message = "Поле name не должно быть пустым")
    private String name;
}
