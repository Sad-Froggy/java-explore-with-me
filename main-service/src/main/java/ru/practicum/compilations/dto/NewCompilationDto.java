package ru.practicum.compilations.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank(message = "Заголовок подборки не может быть null и состоять из пробелов")
    private String title;

}
