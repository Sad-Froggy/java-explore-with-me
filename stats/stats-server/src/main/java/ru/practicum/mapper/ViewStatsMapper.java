package ru.practicum.mapper;

import lombok.NonNull;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

public class ViewStatsMapper {
    public static ViewStats toViewStats(@NonNull ViewStatsDto dto) {
        return ViewStats.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .hits(dto.getHits())
                .build();
    }

    public static ViewStatsDto toViewStatsDto(@NonNull ViewStats model) {
        return ViewStatsDto.builder()
                .app(model.getApp())
                .uri(model.getUri())
                .hits(model.getHits())
                .build();
    }
}
