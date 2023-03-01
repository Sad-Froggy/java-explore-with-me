package ru.practicum.service;

import ru.practicum.EndPointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndPointHitService {
    EndPointHitDto post(EndPointHitDto endPointHitDto);

    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
