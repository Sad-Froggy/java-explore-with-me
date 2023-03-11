package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.model.Event;
import ru.practicum.events.state.State;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositorySearch {

    @Query("select event from Event event " +
            "where event.category.id = :categoryId")
    Optional<Event> findByCategoryId(Long categoryId);

    Optional<Event> findByIdAndState(Long eventId, State state);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select event from Event event " +
            "where (lower(event.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(event.description) like lower(concat('%', :text, '%'))) " +
            "and event.category.id in :categories and event.paid = :paid " +
            "and event.eventDate between :rangeStart and :rangeEnd " +
            "and (event.participantLimit <> 0L and event.participantLimit <= event.confirmedRequests) " +
            "and event.state = :state")
    Page<Event> findAllWithFiltering(String text, List<Long> categories, Boolean paid,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                     State state, Pageable pageable);


    @Query("select event from Event event " +
            "where (upper(event.annotation) like upper(concat('%', :text, '%')) " +
            "or upper(event.description) like upper(concat('%', :text, '%'))) " +
            "and event.category.id in :categories and event.paid = :paid " +
            "and event.eventDate between :rangeStart and :rangeEnd " +
            "and (event.participantLimit = 0L or event.participantLimit > event.confirmedRequests) " +
            "and event.state = :state")
    Page<Event> findAllWithFilteringOnlyAvailable(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  State state, Pageable pageable);

    @Query("select event from Event event " +
            "where event.initiator.id in :users and event.category.id in :categories " +
            "and event.state in :eventStates and event.eventDate between :rangeStart and :rangeEnd")
    Page<Event> findAllByFiltering(List<Long> users, List<State> eventStates, List<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select event from Event event " +
            "where event.initiator.id = :userId and event.id = :eventId")
    Optional<Event> findByIdAndInitiatorId(Long userId, Long eventId);

    @Query("select event from Event event " +
            "where event.initiator.id in :users and event.category.id in :categories " +
            "and event.state in :eventStates")
    Page<Event> findAllByFilteringWithoutDate(List<Long> users, List<State> eventStates,
                                              List<Long> categories, Pageable pageable);

    List<Event> findAllByIdIsGreaterThanEqualAndInitiatorIdIs(long from, Long userId, PageRequest pageRequest);

    List<Event> findEventsByInitiator(User user, Pageable pageable);

    Optional<Event> findEventByIdAndInitiator(Long id, User user);

    List<Event> findAllByCategoryId(Long categoryId);







}
