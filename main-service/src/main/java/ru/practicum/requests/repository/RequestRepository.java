package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.users.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Boolean existsRequestByEventAndRequester(Event event, User requester);

    List<ParticipationRequest> findByEvent(Event event);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventIn(List<Event> events);
}
