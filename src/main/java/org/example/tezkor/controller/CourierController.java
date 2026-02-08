package org.example.tezkor.controller;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.CourierLocationRequest;
import org.example.tezkor.enums.Courier;
import org.example.tezkor.enums.Order;
import org.example.tezkor.service.CourierOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courier")
@RequiredArgsConstructor
@CrossOrigin
public class CourierController {

    private final CourierOrderService courierOrderService;

    // Barcha tayyor buyurtmalarni ko'rish
    @GetMapping("/available-orders")
    public ResponseEntity<List<Order>> getAvailableOrders() {
        List<Order> orders = courierOrderService.getAvailableOrders();
        return ResponseEntity.ok(orders);
    }

    // Buyurtmani qabul qilish
    @PostMapping("/accept-order/{orderId}")
    public ResponseEntity<Order> acceptOrder(
            @PathVariable Long orderId,
            @RequestParam Long courierId) {
        Order order = courierOrderService.acceptOrderByCourier(orderId, courierId);
        return ResponseEntity.ok(order);
    }

    // Buyurtmani yetkazib berish
    @PostMapping("/deliver-order/{orderId}")
    public ResponseEntity<Order> deliverOrder(
            @PathVariable Long orderId,
            @RequestParam Long courierId) {
        Order order = courierOrderService.deliverOrder(orderId, courierId);
        return ResponseEntity.ok(order);
    }

    // Kuryer joylashuvini yangilash
    @PostMapping("/{courierId}/location")
    public ResponseEntity<Courier> updateLocation(
            @PathVariable Long courierId,
            @RequestBody CourierLocationRequest request) {
        Courier courier = courierOrderService.updateCourierLocation(
                courierId,
                request.getLatitude(),
                request.getLongitude()
        );
        return ResponseEntity.ok(courier);
    }

    // Kuryer statusini o'zgartirish (band/bo'sh)
    @PostMapping("/{courierId}/toggle-availability")
    public ResponseEntity<Courier> toggleAvailability(@PathVariable Long courierId) {
        Courier courier = courierOrderService.toggleCourierAvailability(courierId);
        return ResponseEntity.ok(courier);
    }




}