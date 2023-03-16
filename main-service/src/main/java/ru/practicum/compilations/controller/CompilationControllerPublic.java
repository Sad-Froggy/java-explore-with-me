package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.service.CompilationServicePublic;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationControllerPublic {
    private final CompilationServicePublic compilationServicePublic;

    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(name = "pinned", defaultValue = "false") Boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос списка подборок событий от пользователя");
        return compilationServicePublic.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(
            @PathVariable("compId") @Positive Long compId) {
        log.info("Запрос подборки событий по id от пользователя - " + compId);
        return compilationServicePublic.getCompilation(compId);
    }
}
