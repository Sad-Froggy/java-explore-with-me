package ru.practicum.repository;

import org.springframework.data.jpa.repository.*;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndPointHit, Integer> {

    @Query(value = "select new ru.practicum.model.ViewStats(e.app, e.uri, count(e)) " +
            "from EndPointHit e " +
            "where e.timestamp >= ?1 and e.timestamp <= ?2 and e.uri in ?3 " +
            "group by e.app, e.uri " +
            "order by count(e) desc")
    List<ViewStats> findAllByParameters(LocalDateTime start, LocalDateTime end, String[] uris, Class<ViewStats> type);

    @Query(value = "select new ru.practicum.model.ViewStats(e.app, e.uri, count(e)) " +
            "from EndPointHit e " +
            "where e.timestamp >= ?1 and e.timestamp <= ?2 and e.uri in ?3 " +
            "group by e.app, e.uri, e.ip " +
            "order by count(e) desc")
    List<ViewStats> findAllUniqueIpByParameters(LocalDateTime start, LocalDateTime end, String[] uris, Class<ViewStats> type);
}
