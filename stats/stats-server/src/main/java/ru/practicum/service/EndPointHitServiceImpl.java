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


    @Transactional
    @Override
    public EndPointHitDto post(EndPointHitDto endPointHitDto) {
        EndPointHit endPointHit = EndPointHitMapper.toEndPointHit(endPointHitDto);
        endPointHit.setTimestamp(LocalDateTime.now());
        return EndPointHitMapper.toEndPointHitDto(repository.save(endPointHit));
    }

    @Override
    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new NotValidException("нижняя граница времени позже верхней");
        }
        List<ViewStats> result = unique ? repository.findDistinctAllStatsWithFilter(uris, start, end)
                : repository.findAllStatsWithFilter(uris, start, end);
        return result.stream().map(ViewStatsMapper::toViewStatsDto).collect(Collectors.toList());
    }
}