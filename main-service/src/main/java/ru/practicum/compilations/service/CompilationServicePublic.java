package ru.practicum.compilations.service;


import ru.practicum.compilations.dto.CompilationDto;

import java.util.List;

public interface CompilationServicePublic {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compId);

}
