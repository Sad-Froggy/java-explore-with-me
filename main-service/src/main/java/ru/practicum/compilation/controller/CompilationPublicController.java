package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationServicePub;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationPublicController {
    private final CompilationServicePub compilationServicePub;

    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(name = "pinned", defaultValue = "false") Boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получение подборок событий по параметрам pinned: {}, from: {}, size: {}", pinned, from, size);
        return compilationServicePub.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(
            @PathVariable("compId") @Positive Long compId) {
        log.info(" {}", compId);
        return compilationServicePub.getById(compId);
    }
}
