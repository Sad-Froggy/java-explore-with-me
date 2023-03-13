package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositorySearch {

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select event from Event event " +
            "where event.initiator.id = :userId and event.id = :eventId")
    Optional<Event> findByIdAndInitiatorId(Long userId, Long eventId);

    List<Event> findAllByCategoryId(Long categoryId);







}
