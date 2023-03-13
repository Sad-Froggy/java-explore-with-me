package ru.practicum.compilations.dto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.events.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    List<EventShortDto> events;
    @NotNull(message = "Id не может быть null")
    Long id;
    @NotNull(message = "Закрепление подборки на главной странице сайта не может быть null")
    Boolean pinned;
    @NotBlank(message = "Заголовок подборки не может быть null и состоять из пробелов")
    String title;
}
