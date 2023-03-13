package ru.practicum.compilations.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UpdateCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
