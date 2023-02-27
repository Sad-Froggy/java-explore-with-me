package ru.practicum.mapper;

import lombok.NonNull;
import ru.practicum.EndPointHitDto;
import ru.practicum.model.EndPointHit;


public class EndPointHitMapper {
    public static EndPointHit toEndPointHit(@NonNull EndPointHitDto dto) {
        return EndPointHit.builder()
                .id(dto.getId())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .app(dto.getApp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static EndPointHitDto toEndPointHitDto(@NonNull EndPointHit model) {
        return EndPointHitDto.builder()
                .id(model.getId())
                .uri(model.getUri())
                .ip(model.getIp())
                .app(model.getApp())
                .timestamp(model.getTimestamp())
                .build();
    }
}
