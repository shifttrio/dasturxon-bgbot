package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.RevenueReportDto;
import org.example.tezkor.enums.*;
import org.example.tezkor.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final CourierWorkSessionRepository courierWorkSessionRepository;

    // ADMIN PANEL - Do'kon tushumi
    public RevenueReportDto getDailyRevenue(Long shopId, LocalDate date) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Do'kon topilmadi"));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByShopAndStatusAndCreatedAtBetween(
                shop, OrderStatus.DELIVERED, startOfDay, endOfDay
        );

        return calculateRevenue(orders, "Kunlik");
    }

    public RevenueReportDto getWeeklyRevenue(Long shopId, LocalDate startDate) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Do'kon topilmadi"));

        LocalDateTime startOfWeek = startDate.atStartOfDay();
        LocalDateTime endOfWeek = startDate.plusDays(7).atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByShopAndStatusAndCreatedAtBetween(
                shop, OrderStatus.DELIVERED, startOfWeek, endOfWeek
        );

        return calculateRevenue(orders, "Haftalik");
    }

    public RevenueReportDto getMonthlyRevenue(Long shopId, int year, int month) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Do'kon topilmadi"));

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);

        LocalDateTime startOfMonth = firstDay.atStartOfDay();
        LocalDateTime endOfMonth = lastDay.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByShopAndStatusAndCreatedAtBetween(
                shop, OrderStatus.DELIVERED, startOfMonth, endOfMonth
        );

        return calculateRevenue(orders, "Oylik");
    }

    // KURYER PANEL - Daromad
    public RevenueReportDto getCourierDailyEarnings(Courier courier, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByCourierAndStatusAndDeliveredAtBetween(
                courier, OrderStatus.DELIVERED, startOfDay, endOfDay
        );

        Long workMinutes = courierWorkSessionRepository.getTotalWorkMinutes(
                courier, startOfDay, endOfDay
        );

        RevenueReportDto report = calculateRevenue(orders, "Kunlik");
        report.setWorkHours(workMinutes != null ? workMinutes / 60.0 : 0.0);

        return report;
    }

    public RevenueReportDto getCourierWeeklyEarnings(Courier courier, LocalDate startDate) {
        LocalDateTime startOfWeek = startDate.atStartOfDay();
        LocalDateTime endOfWeek = startDate.plusDays(7).atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByCourierAndStatusAndDeliveredAtBetween(
                courier, OrderStatus.DELIVERED, startOfWeek, endOfWeek
        );

        Long workMinutes = courierWorkSessionRepository.getTotalWorkMinutes(
                courier, startOfWeek, endOfWeek
        );

        RevenueReportDto report = calculateRevenue(orders, "Haftalik");
        report.setWorkHours(workMinutes != null ? workMinutes / 60.0 : 0.0);

        return report;
    }

    public RevenueReportDto getCourierMonthlyEarnings(Courier courier, int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);

        LocalDateTime startOfMonth = firstDay.atStartOfDay();
        LocalDateTime endOfMonth = lastDay.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByCourierAndStatusAndDeliveredAtBetween(
                courier, OrderStatus.DELIVERED, startOfMonth, endOfMonth
        );

        Long workMinutes = courierWorkSessionRepository.getTotalWorkMinutes(
                courier, startOfMonth, endOfMonth
        );

        RevenueReportDto report = calculateRevenue(orders, "Oylik");
        report.setWorkHours(workMinutes != null ? workMinutes / 60.0 : 0.0);

        return report;
    }

    private RevenueReportDto calculateRevenue(List<Order> orders, String period) {
        Double totalRevenue = orders.stream()
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0.0)
                .sum();

        Double totalDeliveryFee = orders.stream()
                .mapToDouble(o -> o.getDeliveryFee() != null ? o.getDeliveryFee() : 0.0)
                .sum();

        Double totalDiscount = orders.stream()
                .mapToDouble(o -> o.getDiscountAmount() != null ? o.getDiscountAmount() : 0.0)
                .sum();

        return RevenueReportDto.builder()
                .period(period)
                .orderCount(orders.size())
                .totalRevenue(totalRevenue)
                .totalDeliveryFee(totalDeliveryFee)
                .totalDiscount(totalDiscount)
                .netRevenue(totalRevenue - totalDiscount)
                .build();
    }
}