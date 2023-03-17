package ru.practicum.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
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
@AllArgsConstructor
public class EwmObjectFinder {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CompilationRepository compilationRepository;

    private final CommentRepository commentRepository;

    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + id + " не найден"));
    }

    public void checkUserExistenceById(Long id) {
        if (id == null) {
            throw new ValidationException("Id пользователя не указан");
        }
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователь с id - " + id + "не найден");
        }
    }

    public Event findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id - " + id + "не найдено"));
    }

    public void checkEventExistenceById(Long id) {
        if (id == null) {
            throw new ValidationException("Id события не указан");
        }
        if (eventRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Событие с id - " + id + "не найдено");
        }
    }

    public Category findCategory(Long id) {
        if (id == null) throw new ValidationException("Id категории не указан");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id - " + id + "не найдена"));
    }

    public void checkCategoryExistenceById(Long id) {
        if (id == null) throw new ValidationException("Id категории не указан");
        if (categoryRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Категория с id - " + id + "не найдена");
        }
    }

    public ParticipationRequest findRequest(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос на участие с id - " + id + "не найден"));
    }

    public Compilation findCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id - " + id + "не найдена"));
    }

    public Comment findComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с id - " + id + "не найдена"));
    }

    public void checkCommentExistenceById(Long id) {
        if (id == null) throw new ValidationException("Id комментария не указан");
        if (commentRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Комментарий с id - " + id + "не найден");
        }
    }

}
