package ru.practicum.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

@Service
public class EwmObjectFinder {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CompilationRepository compilationRepository;

    @Autowired
    public EwmObjectFinder(UserRepository userRepository,
                                   CategoryRepository categoryRepository,
                                   EventRepository eventRepository,
                                   RequestRepository requestRepository,
                                   CompilationRepository compilationRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.compilationRepository = compilationRepository;
    }


    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + id + "не найден"));
    }

    public Event findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id - " + id + "не найдено"));
    }

    public Category findCategory(Long id) {
        if (id == null) throw new ValidationException("Id категории не указан");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id - " + id + "не найдена"));
    }

    public ParticipationRequest findRequest(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос на участие с id - " + id + "не найден"));
    }

    public Compilation findCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id - " + id + "не найдена"));
    }
}
