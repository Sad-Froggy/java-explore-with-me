package ru.practicum.compilations.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationDto;

public interface CompilationServiceAdmin {
    @Transactional
    CompilationDto createCompilation(NewCompilationDto compilationDto);

    @Transactional
    void deleteCompilation(Long compId);

    @Transactional
    CompilationDto updateCompilation(Long compId, UpdateCompilationDto updateCompilationDto);
}
