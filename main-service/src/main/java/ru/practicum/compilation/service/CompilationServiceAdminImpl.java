package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationServicePub servicePub;
    private final CompilationMapper mapper;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = mapper.toCompilation(newCompilationDto, events);
        CompilationDto compilationDto = mapper.toCompilationDto(compilationRepository.save(compilation));
        log.info("Добавлена новая подборка событий {}", compilationDto);
        return compilationDto;
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        servicePub.getByIdForService(compId);
        compilationRepository.deleteById(compId);
        log.info("Удалена подборка событий с id: {}", compId);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = servicePub.getByIdForService(compId);
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        compilation.setEvents(events);
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        CompilationDto compilationDto = mapper.toCompilationDto(compilation);
        log.info("Обновлена подборка событий {}", compilationDto);
        return compilationDto;
    }
}

