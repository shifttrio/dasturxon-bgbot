package org.example.tezkor.controller;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.RevenueReportDto;
import org.example.tezkor.enums.Courier;
import org.example.tezkor.repository.CourierRepository;
import org.example.tezkor.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final CourierRepository courierRepository;

    // ADMIN - Kunlik hisobot
    @GetMapping("/shop/{shopId}/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<RevenueReportDto> getDailyRevenue(
            @PathVariable Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(analyticsService.getDailyRevenue(shopId, date));
    }

    // ADMIN - Haftalik hisobot
    @GetMapping("/shop/{shopId}/weekly")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<RevenueReportDto> getWeeklyRevenue(
            @PathVariable Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        return ResponseEntity.ok(analyticsService.getWeeklyRevenue(shopId, startDate));
    }

    // ADMIN - Oylik hisobot
    @GetMapping("/shop/{shopId}/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<RevenueReportDto> getMonthlyRevenue(
            @PathVariable Long shopId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(analyticsService.getMonthlyRevenue(shopId, year, month));
    }

    // KURYER - Kunlik daromad
    @GetMapping("/courier/{courierId}/daily")
    @PreAuthorize("hasAnyRole('COURIER', 'OWNER')")
    public ResponseEntity<RevenueReportDto> getCourierDailyEarnings(
            @PathVariable Long courierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Kuryer topilmadi"));
        return ResponseEntity.ok(analyticsService.getCourierDailyEarnings(courier, date));
    }

    // KURYER - Haftalik daromad
    @GetMapping("/courier/{courierId}/weekly")
    @PreAuthorize("hasAnyRole('COURIER', 'OWNER')")
    public ResponseEntity<RevenueReportDto> getCourierWeeklyEarnings(
            @PathVariable Long courierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Kuryer topilmadi"));
        return ResponseEntity.ok(analyticsService.getCourierWeeklyEarnings(courier, startDate));
    }

    // KURYER - Oylik daromad
    @GetMapping("/courier/{courierId}/monthly")
    @PreAuthorize("hasAnyRole('COURIER', 'OWNER')")
    public ResponseEntity<RevenueReportDto> getCourierMonthlyEarnings(
            @PathVariable Long courierId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Kuryer topilmadi"));
        return ResponseEntity.ok(analyticsService.getCourierMonthlyEarnings(courier, year, month));
    }
}