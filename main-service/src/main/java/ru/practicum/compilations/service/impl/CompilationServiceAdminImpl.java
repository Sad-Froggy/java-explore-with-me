package ru.practicum.compilations.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationDto;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.compilations.service.CompilationServiceAdmin;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.util.EwmObjectFinder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EwmObjectFinder finder;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
        log.info("Добавлена подборка событий - " + compilationDto.getTitle());
        return compilationDto;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        finder.findCompilation(compId);
        compilationRepository.deleteById(compId);
        log.info("Удалена подборка событий с id - " + compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto updateDto) {
        Compilation compilation = finder.findCompilation(compId);
        if (updateDto.getEvents() != null) {
            compilation.setEvents(updateDto.getEvents()
                    .stream()
                    .map(finder::findEvent)
                    .collect(Collectors.toList()));
        }
        Optional.ofNullable(updateDto.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(updateDto.getTitle()).ifPresent(compilation::setTitle);
        Compilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Обновлена подборка событий + " + updatedCompilation.getTitle());
        return CompilationMapper.toCompilationDto(updatedCompilation);
    }
}

