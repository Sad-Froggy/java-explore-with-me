package ru.practicum.service;

import ru.practicum.EndPointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndPointHitService {
    void post(EndPointHitDto endPointHitDto);

    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
