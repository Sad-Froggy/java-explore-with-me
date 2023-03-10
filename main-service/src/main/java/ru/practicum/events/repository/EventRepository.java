package ru.practicum.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositorySearch {

    List<Event> findAllByIdIsGreaterThanEqualAndInitiatorIdIs(long from, Long userId, PageRequest pageRequest);

    List<Event> findAllByCategoryId(Long categoryId);
}
