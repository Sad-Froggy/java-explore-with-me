package ru.practicum.controller;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndPointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.EndPointHitService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class StatsServerController {

    private final EndPointHitService endPointHitService;

    @PostMapping("/hit")
    public void post(@RequestBody @Valid EndPointHitDto endPointHitDto) {
        log.info("Запрос создания события в сервере статистики");
        ResponseEntity.ok(endPointHitService.post(endPointHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> get(HttpServletRequest request,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam (required = false) List<String> uris,
                                  @RequestParam (defaultValue = "false") boolean unique) {
        log.info("Запрос получения статистики");
        return ResponseEntity.ok(endPointHitService.get(start, end, uris, unique));
    }
}
