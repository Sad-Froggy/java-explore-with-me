package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.events.model.Location;
import ru.practicum.events.state.StateAction;


import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Size(max = 500)
    private String annotation;
    private Long category;
    @Size(max = 1000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Boolean disableComments;
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(max = 200)
    private String title;
}
