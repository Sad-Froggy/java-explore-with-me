package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatsClientApp {

    protected final RestTemplate rest;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClientApp(@Value("${stats-client-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    protected ResponseEntity<List<ViewStatsDto>> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String i : uris) {
            stringBuilder.append("uris=").append(i).append("&");
        }
        String query = String.format("/stats?start=%s&end=%s&%sunique=%s}", start.format(formatter), end.format(formatter), stringBuilder, unique);
        String queryEncoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return rest.exchange(queryEncoded, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    protected void post(EndPointHitDto endPointHit) {
        HttpEntity<EndPointHitDto> requestEntity = new HttpEntity<>(endPointHit);
        rest.exchange("/hit", HttpMethod.POST, requestEntity, EndPointHitDto.class);
    }
}