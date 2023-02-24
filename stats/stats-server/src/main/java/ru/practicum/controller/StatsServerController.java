package ru.practicum.controller;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
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
public class StatsServerController {

    private final EndPointHitService endPointHitService;

    @PostMapping("/hit")
    public void post(@RequestBody @Valid EndPointHitDto endPointHitDto) {
        endPointHitService.post(endPointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> get(HttpServletRequest request, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam String[] uris,
                                  @RequestParam (defaultValue = "false") boolean unique) {
        return endPointHitService.get(start, end, uris, unique);
    }
}
