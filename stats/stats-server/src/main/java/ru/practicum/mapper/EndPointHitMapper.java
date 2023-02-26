package ru.practicum.mapper;

import ru.practicum.EndPointHitDto;
import ru.practicum.model.EndPointHit;


public class EndPointHitMapper {
    public static EndPointHit toEndPointHit(EndPointHitDto dto) {
        return EndPointHit.builder()
                .id(dto.getId())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .app(dto.getApp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static EndPointHitDto toEndPointHitDto(EndPointHit model) {
        return EndPointHitDto.builder()
                .id(model.getId())
                .uri(model.getUri())
                .ip(model.getIp())
                .app(model.getApp())
                .timestamp(model.getTimestamp())
                .build();
    }
}
