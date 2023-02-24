package ru.practicum.service;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import ru.practicum.EndPointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.NotValidException;
import ru.practicum.mapper.EndPointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EndPointHitServiceImpl implements EndPointHitService {

    private final StatsRepository repository;
    private final EndPointHitMapper endPointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Transactional
    @Override
    public void post(EndPointHitDto endPointHitDto) {
        EndPointHit endPointHit = endPointHitMapper.toEndPointHit(endPointHitDto);
        repository.save(endPointHit);
    }

    @Override
    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new NotValidException("нижняя граница времени позже верхней");
        }
        List<ViewStats> viewStats;
        if (unique) {
            viewStats = repository.findAllUniqueIpByParameters(start, end, uris, ViewStats.class);
        } else {
            viewStats = repository.findAllByParameters(start, end, uris, ViewStats.class);
        }
        return viewStats.stream().map(viewStatsMapper::toViewStatsDto).collect(Collectors.toList());
    }
}
