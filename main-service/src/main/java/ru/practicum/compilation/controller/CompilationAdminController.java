package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.CompilationServiceAdmin;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationAdminController {
    private final CompilationServiceAdmin compilationServiceAdmin;

    @PostMapping
    public CompilationDto create(
            @RequestBody @Validated NewCompilationDto newCompilationDto) {
        log.info("Добавление новой подборки событий {}", newCompilationDto);
        return compilationServiceAdmin.create(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void delete(
            @PathVariable("compId") @Positive Long compId) {
        log.info("Удаление подборки по id: {}", compId);
        compilationServiceAdmin.delete(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(
            @PathVariable("compId") @Positive Long compId,
            @RequestBody @Validated NewCompilationDto newCompilationDto) {
        log.info("Обновление подборки {} с id: {}", newCompilationDto, compId);
        return compilationServiceAdmin.update(compId, newCompilationDto);
    }
}
