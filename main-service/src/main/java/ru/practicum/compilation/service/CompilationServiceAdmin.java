package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

public interface CompilationServiceAdmin {
    CompilationDto create(NewCompilationDto compilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, NewCompilationDto newCompilationDto);
}
