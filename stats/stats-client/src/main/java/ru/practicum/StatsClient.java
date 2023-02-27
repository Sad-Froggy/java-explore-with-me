package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatsClient {

    protected final RestTemplate rest;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(@Value("${stats-client-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    protected ResponseEntity<List<ViewStatsDto>> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append("uris=");
        for (String uri : uris) {
            uriBuilder.append(uri).append("&");
        }
        String query = String.format("/stats?start=%s&end=%s&%sunique=%s}", start.format(formatter), end.format(formatter), uriBuilder, unique);
        return rest.exchange(query, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    protected void post(EndPointHitDto endPointHit) {
        HttpEntity<EndPointHitDto> requestEntity = new HttpEntity<>(endPointHit);
        rest.exchange("/hit", HttpMethod.POST, requestEntity, EndPointHitDto.class);
    }
}