package ru.practicum.compilations.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.compilations.service.CompilationServicePublic;
import ru.practicum.util.EwmObjectFinder;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServicePublicImpl implements CompilationServicePublic {
    private final CompilationRepository compilationRepository;

    private final EwmObjectFinder finder;

    @Transactional
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        from = from.equals(size) ? from / size : from;
        final PageRequest pageRequest = PageRequest.of(from, size, Sort.by(DESC, "id"));
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest).getContent();
        List<CompilationDto> compilationDtos =
                compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
        log.info("Получена подборка событий {}", compilationDtos);
        return compilationDtos;
    }

    @Transactional
    @Override
    public CompilationDto getCompilation(Long compId) {
        log.info("Поиск подборки событий по id: {}", compId);
        return CompilationMapper.toCompilationDto(finder.findCompilation(compId));
    }
}
