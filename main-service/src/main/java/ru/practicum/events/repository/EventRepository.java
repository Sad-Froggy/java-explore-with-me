package ru.practicum.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositorySearch {

    Optional<Event> findByCategoryId(int category_id);

    List<Event> findAllByIdIsGreaterThanEqualAndInitiatorIdIs(long from, Long userId, PageRequest pageRequest);
}
