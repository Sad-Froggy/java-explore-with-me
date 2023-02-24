package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.EndPointHitDto;
import ru.practicum.model.EndPointHit;


@Mapper(componentModel = "spring")
public interface EndPointHitMapper {
    EndPointHit toEndPointHit(EndPointHitDto source);

    EndPointHitDto toEndPointHitDto(EndPointHit destination);
}
