package ru.practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndPointHit, Integer> {

    @Query(value = "select NEW ru.practicum.model.ViewStats(e.app, e.uri, COUNT(e.ip))  " +
            "from EndPointHit e " +
            " where (e.uri in (:uris) or :uris is null)" +
            "and e.timestamp between :start and :end" +
            " group by e.uri, e.app" +
            " order by COUNT(e.ip) desc ")
    List<ViewStats> findAllStatsWithFilter(@Param("uris") List<String> listUris,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query(value = "select NEW ru.practicum.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip))  " +
            "from EndPointHit e " +
            " where (e.uri in (:uris) or :uris is null) " +
            " and e.timestamp between :start and :end" +
            " group by e.uri, e.app " +
            " order by COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findDistinctAllStatsWithFilter(@Param("uris") List<String> listUris,
                                                   @Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end);

}
