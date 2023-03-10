package ru.practicum.events.mapper;


import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.users.mapper.UserMapper;

public class EventMapper {

    public static Event newEventDtoToEvent(NewEventDto e, Category category) {
        return Event.builder()
                .annotation(e.getAnnotation())
                .category(category)
                .description(e.getDescription())
                .eventDate(e.getEventDate())
                .paid(e.isPaid())
                .participantLimit(e.getParticipantLimit())
                .requestModeration(e.getRequestModeration())
                .title(e.getTitle())
                .build();
    }

    public static EventFullDto eventToEventFullDto(Event e) {
        return EventFullDto.builder()
                .annotation(e.getAnnotation())
                .category(CategoryMapper.toCategoryDto(e.getCategory()))
                .createdOn(e.getCreatedOn())
                .description(e.getDescription())
                .eventDate(e.getEventDate())
                .id(e.getId())
                .initiator(UserMapper.toUserDto(e.getInitiator()))
                .paid(e.getPaid())
                .participantLimit(e.getParticipantLimit())
                .publishedOn(e.getPublishedOn())
                .requestModeration(e.isRequestModeration())
                .state(e.getState())
                .title(e.getTitle())
                .views(0L)
                .build();
    }

    public static EventShortDto eventToEventShortDto(Event e) {
        return EventShortDto.builder()
                .annotation(e.getAnnotation())
                .category(CategoryMapper.toCategoryDto(e.getCategory()))
                .eventDate(e.getEventDate())
                .id(e.getId())
                .initiator(UserMapper.toUserShortDto(e.getInitiator()))
                .paid(e.getPaid())
                .title(e.getTitle())
                .build();
    }

}
