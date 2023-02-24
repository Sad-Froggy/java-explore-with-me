package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {
    ViewStats toViewStats(ViewStatsDto source);

    ViewStatsDto toViewStatsDto(ViewStats destination);
}
