package org.example.tezkor.repository;

import org.example.tezkor.enums.CourierWorkSession;
import org.example.tezkor.enums.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourierWorkSessionRepository extends JpaRepository<CourierWorkSession, Long> {

    Optional<CourierWorkSession> findByCourierAndIsActiveTrue(Courier courier);

    List<CourierWorkSession> findByCourier(Courier courier);

    List<CourierWorkSession> findByCourierAndStartTimeBetween(
            Courier courier,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, s.startTime, COALESCE(s.endTime, CURRENT_TIMESTAMP))) " +
            "FROM CourierWorkSession s WHERE s.courier = :courier AND " +
            "s.startTime BETWEEN :start AND :end")
    Long getTotalWorkMinutes(Courier courier, LocalDateTime start, LocalDateTime end);
}