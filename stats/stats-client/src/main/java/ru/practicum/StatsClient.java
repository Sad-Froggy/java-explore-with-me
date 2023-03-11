package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient  {

    @Autowired
    public StatsClient(@Value("${stats-client-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique);
        return get(parameters);
    }

    public ResponseEntity<Object> post(HttpServletRequest request, LocalDateTime timeStamp, String app) {
        EndPointHitDto endpointHit = new EndPointHitDto();
        endpointHit.setApp(app);
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setTimestamp(timeStamp);
        log.info("[HTTP Stats Client] сохранение статистики {}", endpointHit);
        return post(endpointHit);
    }
}