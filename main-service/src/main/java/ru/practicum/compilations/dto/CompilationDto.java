package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.events.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class CompilationDto {
    private List<EventShortDto> events;
    @NotNull(message = "Id не может быть null")
    private Long id;
    @NotNull(message = "Закрепление подборки на главной странице сайта не может быть null")
    private Boolean pinned;
    @NotBlank(message = "Заголовок подборки не может быть null и состоять из пробелов")
    private String title;
}
