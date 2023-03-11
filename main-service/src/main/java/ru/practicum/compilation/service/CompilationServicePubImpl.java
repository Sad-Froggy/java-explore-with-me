package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServicePubImpl implements CompilationServicePub {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper mapper;


    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        from = from.equals(size) ? from / size : from;
        final PageRequest pageRequest = PageRequest.of(from, size, Sort.by(DESC, "id"));
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest).getContent();
        List<CompilationDto> compilationDtos =
                compilations.stream().map(mapper::toCompilationDto).collect(Collectors.toList());
        log.info("Получена подборка событий {}", compilationDtos);
        return compilationDtos;
    }

    @Override
    public CompilationDto getById(Long compId) {
        log.info("Поиск подборки событий по id: {}", compId);
        return mapper.toCompilationDto(findById(compId));
    }

    @Override
    public Compilation getByIdForService(Long compId) {
        log.info("Поиск подборки событий по id: {}", compId);
        return findById(compId);
    }


    private Compilation findById(Long comId) {
        Compilation compilation = compilationRepository.findById(comId).orElseThrow(
                () -> new NotFoundException(String.format("Подборка событий с id: %s не найдена", comId)));
        log.info("Найдена подборка событий с id: {}", comId);
        return compilation;
    }
}
