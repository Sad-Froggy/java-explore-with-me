package ru.practicum.compilation.service;


import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationServicePub {
    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compId);

    Compilation getByIdForService(Long compId);
}
