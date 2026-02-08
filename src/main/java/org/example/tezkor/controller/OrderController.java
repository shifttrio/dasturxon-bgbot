package org.example.tezkor.controller;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.OrderRequest;
import org.example.tezkor.enums.Order;
import org.example.tezkor.enums.OrderStatus;
import org.example.tezkor.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")  // 👈 Bu qatorni qo'shing

public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(request);
        return ResponseEntity.ok(order);
    }


    // Restoran buyurtmani qabul qiladi
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Order> acceptOrder(@PathVariable Long orderId) {
        Order order = orderService.acceptOrder(orderId);
        return ResponseEntity.ok(order);
    }

    // Buyurtma statusini o'zgartirish
    @PostMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        Order order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }

    // Restoran buyurtmalarini ko'rish
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Order>> getShopOrders(@PathVariable Long shopId) {
        List<Order> orders = orderService.getShopOrders(shopId);
        return ResponseEntity.ok(orders);
    }

    // Buyurtma detallari
    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable Long orderId) {
        Map<String, Object> details = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(details);
    }

    // KUNLIK HISOBOT
    @GetMapping("/reports/daily/{shopId}")
    public ResponseEntity<Map<String, Object>> getDailyReport(
            @PathVariable Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Map<String, Object> report = orderService.getDailyReport(shopId, date);
        return ResponseEntity.ok(report);
    }

    // HAFTALIK HISOBOT
    @GetMapping("/reports/weekly/{shopId}")
    public ResponseEntity<Map<String, Object>> getWeeklyReport(
            @PathVariable Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        Map<String, Object> report = orderService.getWeeklyReport(shopId, startDate);
        return ResponseEntity.ok(report);
    }

    // OYLIK HISOBOT
    @GetMapping("/reports/monthly/{shopId}")
    public ResponseEntity<Map<String, Object>> getMonthlyReport(
            @PathVariable Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        Map<String, Object> report = orderService.getMonthlyReport(shopId, startDate);
        return ResponseEntity.ok(report);
    }

    // TELEFON RAQAM BO'YICHA BUYURTMALAR
    @GetMapping("/by-phone")
    public ResponseEntity<List<Map<String, Object>>> getOrdersByPhone(
            @RequestParam String phoneNumber) {

        List<Map<String, Object>> orders = orderService.getOrdersByPhone(phoneNumber);
        return ResponseEntity.ok(orders);
    }




}