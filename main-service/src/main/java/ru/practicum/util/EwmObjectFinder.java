package ru.practicum.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class EwmObjectFinder {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    public Category getCategoryIfExist(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(Category.class.getSimpleName() + " not found"));
    }

    public User getUserIfExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + " not found"));
    }

    public Event getEventIfExist(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class.getSimpleName() + " not found"));
    }

    public ParticipationRequest getRequestIfExist(Long reqId) {
        return requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(ParticipationRequest.class.getSimpleName() + " not found"));
    }
}
