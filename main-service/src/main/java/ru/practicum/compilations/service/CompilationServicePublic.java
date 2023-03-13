package ru.practicum.compilations.service;


import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;

import java.util.List;

public interface CompilationServicePublic {
    @Transactional
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    @Transactional
    CompilationDto getCompilation(Long compId);

}
