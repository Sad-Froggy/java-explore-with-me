package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventSort;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.state.State;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServicePublic {
    private final StatsClient statsClient;
    private final EventRepository repository;
    private final EventMapper mapper;
    private static final String APP = "ewm-main-service";

    private static final Long YEARS_MORE = 33L;
    private static final Long ONE = 1L;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public List<EventShortDto> getAllWithFiltering(
            HttpServletRequest request, String text, List<Long> categories, Boolean paid,
            String rangeStart, String rangeEnd, Boolean onlyAvailable, EventSort sort,
            Integer from, Integer size) {
        from = from.equals(size) ? from / size : from;
        final PageRequest pageRequest = PageRequest.of(from, size, Sort.by(DESC, sort.getColumn()));
        List<Event> events = onlyAvailable ?
                repository.findAllWithFilteringOnlyAvailable(text, categories, paid, convertStart(rangeStart),
                        convertEnd(rangeEnd), State.PUBLISHED, pageRequest).getContent() :
                repository.findAllWithFiltering(text, categories, paid, convertStart(rangeStart),
                        convertEnd(rangeEnd), State.PUBLISHED, pageRequest).getContent();

        events.forEach(event -> event.setViews(event.getViews() + ONE));

        createHit(request);
        List<EventShortDto> eventShorts =
                events.stream().map(mapper::toEventShortDto).collect(Collectors.toList());
        log.info("Получены события {}", eventShorts);
        return eventShorts;
    }

    @Transactional
    public EventFullDto getById(HttpServletRequest request, Long eventId) {
        Event event = findByIdAndState(eventId, State.PUBLISHED);

        event.setViews(event.getViews() + ONE);

        createHit(request);
        EventFullDto eventFullDto = mapper.toEventFullDto(event);
        log.info("Найдено событие {}", eventFullDto);
        return eventFullDto;
    }

    public Event getByIdForService(Long eventId) {
        return findById(eventId);
    }

    public Boolean getByCategoryIdForService(Long categoryId) {
        if (categoryId == null) throw new DataConflictException("Id не может быть null");
        log.info("Поиск события по категории id: {}", categoryId);
        return findByCategoryId(categoryId);
    }

    private Event findById(Long eventId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("Событие c id: %s не найдено", eventId)));
        log.info("Найдено событие: {}", event);
        return event;
    }

    private Event findByIdAndState(Long eventId, State state) {
        Event event = repository.findByIdAndState(eventId, state).orElseThrow(() -> new NotFoundException(
                String.format("Событие c id: %s не найдено", eventId)));
        log.info("Найдено событие: {}", event);
        return event;
    }

    private Boolean findByCategoryId(Long categoryId) {
        Boolean isNotFind = repository.findByCategoryId(categoryId).isEmpty();
        if (isNotFind) log.info("Событие не найдено по категории с id: {}", categoryId);
        if (!isNotFind) log.info("Событие найдено по категории с id: {}", categoryId);
        return isNotFind;

    }

    private void createHit(HttpServletRequest request) {
        statsClient.post(request, LocalDateTime.now(), APP);
    }

    private LocalDateTime convertStart(String rangeStart) {
        return rangeStart.isEmpty() ? LocalDateTime.now() :
                LocalDateTime.parse(rangeStart, formatter);
    }

    private LocalDateTime convertEnd(String rangeEnd) {
        return rangeEnd.isEmpty() ? LocalDateTime.now().plusYears(YEARS_MORE) :
                LocalDateTime.parse(rangeEnd, formatter);
    }



//    @Transactional(readOnly = true)
//    public List<EventFullDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
//                                     LocalDateTime rangeEnd, long from, int size, String ip, String sort) {
//
//        return eventRepository.findByParametersForPublic(text, categories, paid,
//                rangeStart, rangeEnd, onlyAvailable, PageRequest.of(from, size));
//
//

//        List<Event> events = eventRepository.publicSearch(text, categories, paid, rangeStart, rangeEnd, from, size);
//        if (events.isEmpty()) {
//            return Collections.emptyList();
//        }
//        List<EventFullDto> eventFullDtos = events.stream()
//                .map(EventMapper::eventToEventFullDto).collect(Collectors.toList());
//
//        Map<Long, Long> views = eventCommonService.getStats(events, false);
//
//        eventFullDtos.forEach(e -> e.setViews(views.get(e.getId())));
//
//        List<ParticipationRequest> confirmedRequests = requestService.findConfirmedRequests(events);
//
//        for (EventFullDto eventFullDto : eventFullDtos) {
//            eventFullDto.setConfirmedRequests((int) confirmedRequests.stream()
//                    .filter(r -> r.getEvent().getId().equals(eventFullDto.getId())).count());
//        }
//
//        EndPointHitDto hitDto = EndPointHitDto.builder()
//                .app("main-service")
//                .uri("/events")
//                .ip(ip)
//                .timestamp(LocalDateTime.now())
//                .build();
//        statsClient.post(hitDto);
//        events.forEach(e -> {
//            hitDto.setUri("/events/" + e.getId());
//            statsClient.post(hitDto);
//        });
//        if (sort != null && Sort.valueOf(sort).equals(Sort.VIEWS)) {
//            return eventFullDtos.stream()
//                    .sorted(Comparator.comparing(EventFullDto::getViews)).collect(Collectors.toList());
//        }
//        return eventFullDtos.stream()
//                .sorted(Comparator.comparing(EventFullDto::getEventDate)).collect(Collectors.toList());
    }

























//package ru.practicum.events.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.EndPointHitDto;
//import ru.practicum.StatsClient;
//import ru.practicum.categories.model.Category;
//import ru.practicum.categories.repository.CategoryRepository;
//import ru.practicum.events.dto.EventFullDto;
//import ru.practicum.events.dto.EventShortDto;
//import ru.practicum.events.mapper.EventMapper;
//import ru.practicum.events.model.Event;
//import ru.practicum.events.model.Sort;
//import ru.practicum.events.repository.EventRepository;
//import ru.practicum.events.state.State;
//import ru.practicum.exception.NotFoundException;
//import ru.practicum.requests.model.ParticipationRequest;
//import ru.practicum.requests.service.RequestService;
//import ru.practicum.util.EwmObjectFinder;
//
//import javax.servlet.http.HttpServletRequest;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static ru.practicum.events.mapper.EventMapper.eventToEventFullDto;
//
//@Service
//@RequiredArgsConstructor
//public class EventServicePublic {
//    private final EventRepository eventRepository;
//
//    private final StatsClient statsClient;
//    private final EventService eventCommonService;
//    private final RequestService requestService;
//
//    private final EwmObjectFinder ewmObjectFinder;
//
//    @Transactional(readOnly = true)
//    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
//        if (!event.getState().equals(State.PUBLISHED)) {
//            throw new NotFoundException(event + " not found");
//        }
//        EventFullDto eventFullDto = eventToEventFullDto(event);
//
//        String uri = request.getRequestURI();
//        String ip = request.getRemoteAddr();
//        EndPointHitDto hitDto = EndPointHitDto.builder()
//                .app("APP_NAME")
//                .uri(uri)
//                .ip(ip)
//                .timestamp(LocalDateTime.now())
//                .build();
//        statsClient.post(hitDto);
//        return eventFullDto;
//    }
//
//    @Transactional(readOnly = true)
//    public List<EventFullDto> search(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
//                                     LocalDateTime rangeEnd, long from, int size, HttpServletRequest request, String sort) {
//        List<Event> events = eventRepository.publicSearch(text, categoryIds, paid, rangeStart, rangeEnd, from, size);
//        statsClient.post(new EndPointHitDto(null, "mainService", request.getRequestURI(),
//                request.getRemoteAddr(),
//                LocalDateTime.now()));
//
////        List<Category> categories = categoryIds != null ?
////                categoryIds
////                        .stream()
////                        .map(ewmObjectFinder::getCategoryIfExist)
////                        .collect(Collectors.toList()) :
//                null;
//        if (sort == null) {
//            sort = "EVENT_DATE";
//        }
//        switch (sort) {
//            case "EVENT_DATE":
//                return getSortedEventByDate(
//                        text,
//                        categories,
//                        paid,
//                        rangeStart,
//                        rangeEnd,
//                        from,
//                        size)
//            case "VIEWS":
//                return getSortedEventByViews(EventPublicSearchDto.builder()
//                        .text(text)
//                        .categories(categories)
//                        .paid(paid)
//                        .rangeStart(DateTimeUtils.parseDate(rangeStart))
//                        .rangeEnd(DateTimeUtils.parseDate(rangeEnd))
//                        .onlyAvailable(onlyAvailable)
//                        .from(from)
//                        .size(size)
//                        .build());
//            default:
//                throw new BadRequestException("Сортировать можно только по EVENT_DATE и VIEWS");
//
//        }
//    }
//
//    private List<EventShortDto> getSortedEventByDate(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
//                                                     LocalDateTime rangeEnd, long from, int size) {
//        List<Event> gotEvents = eventRepository.publicSearch(text, categoryIds, paid, rangeStart, rangeEnd, from, size);
//        return gotEvents.stream().map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
//    }
//
//    private List<EventShortDto> getSortedEventByViews(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
//                                                      LocalDateTime rangeEnd, long from, int size) {
//        List<Event> gotEvents = eventRepository.publicSearch(text, categoryIds, paid, rangeStart, rangeEnd, from, size);
//
//        Map<Long, Long> viewsMap = statsClient.viewsMapRequest(gotEvents
//                .stream()
//                .map(Event::getId)
//                .collect(Collectors.toList()));
//
//        gotEvents.sort((Event e1, Event e2) -> {
//            Long v1 = viewsMap.get(e1.getId());
//            Long v2 = viewsMap.get(e2.getId());
//            return Long.compare(v1 != null ? v1 : 0L, v2 != null ? v2 : 0L);
//        });
//        return gotEvents
//                .stream()
//                .skip(searchDto.getFrom())
//                .limit(searchDto.getSize())
//                .map(EventMapper::toShortDto)
//                .collect(Collectors.toList());
//    }
//
//}
