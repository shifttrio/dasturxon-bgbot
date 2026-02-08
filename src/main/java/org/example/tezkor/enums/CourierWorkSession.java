package org.example.tezkor.enums;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "courier_work_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierWorkSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

    private LocalDateTime startTime; // Ishni boshlagan vaqt
    private LocalDateTime endTime;   // Ishni tugatgan vaqt

    // Lokatsiya ma'lumotlari
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;

    private Boolean isActive = true; // Hozir ishlamoqdami

    // Avtomatik hisoblash
    public Long getWorkDurationMinutes() {
        if (startTime == null) return 0L;
        LocalDateTime end = endTime != null ? endTime : LocalDateTime.now();
        return Duration.between(startTime, end).toMinutes();
    }
}