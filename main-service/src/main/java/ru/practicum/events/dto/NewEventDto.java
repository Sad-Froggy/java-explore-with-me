package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.model.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Аннотация не может быть null и состоять из пробелов")
    @Size(max = 500)
    private String annotation;
    @Positive(message = "Id категории не может быть null и отрицательным")
    private Long category;
    @NotBlank(message = "Полное описание не может быть null и состоять из пробелов")
    @Size(max = 1000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    @NotNull(message = "Широта и долгота не может быть null")
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NotBlank(message = "Заголовок события не может быть null и состоять из пробелов")
    @Size(max = 200)
    private String title;
}