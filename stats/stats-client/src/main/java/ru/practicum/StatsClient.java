package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uri, boolean unique) {
        String path;
        Map<String, Object> parameters;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (!uri.isEmpty()) {
            path = "/stats?start={start}&end={end}&uri={uri}&unique={unique}";
            parameters = Map.of(
                    "start", start.format(formatter),
                    "end", end.format(formatter),
                    "uri", uri,
                    "unique", unique
            );
        } else {
            path = "/stats?start={start}&end={end}&unique={unique}";
            parameters = Map.of("start", start.format(formatter), "end", end.format(formatter),
                    "unique", unique
            );
        }
        log.info("StatsClient - запрос статистики по uri " + uri);
        return get(path, parameters);
    }


    public ResponseEntity<Object> post(String uri, String ip, LocalDateTime timeStamp, String app) {
        EndPointHitDto endpointHit = new EndPointHitDto();
        endpointHit.setApp(app);
        endpointHit.setIp(ip);
        endpointHit.setUri(uri);
        endpointHit.setTimestamp(timeStamp);
        log.info("StatsClient - сохранение статистики " + endpointHit.getApp());
        return post("/hit", endpointHit);
    }

}