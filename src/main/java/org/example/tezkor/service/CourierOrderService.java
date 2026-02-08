package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.enums.Courier;
import org.example.tezkor.enums.Order;
import org.example.tezkor.enums.OrderStatus;
import org.example.tezkor.exception.BadRequestException;
import org.example.tezkor.exception.CourierNotAvailableException;
import org.example.tezkor.exception.ResourceNotFoundException;
import org.example.tezkor.repository.CourierRepository;
import org.example.tezkor.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierOrderService {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    // Barcha tayyor buyurtmalarni ko'rish (kuryer olishi mumkin)
    public List<Order> getAvailableOrders() {
        return orderRepository.findByStatus(OrderStatus.READY);
    }

    // Kuryer buyurtmani qabul qiladi
    public Order acceptOrderByCourier(Long orderId, Long courierId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyurtma topilmadi"));

        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Kuryer topilmadi"));

        if (!courier.getAvailable()) {
            throw new CourierNotAvailableException("Kuryer hozir band. Iltimos, avval statusingizni 'Bo'sh' ga o'zgartiring.");
        }

        if (order.getStatus() != OrderStatus.READY) {
            throw new BadRequestException("Buyurtma tayyor emas. Status: " + order.getStatus());
        }

        order.setCourier(courier);
        order.setStatus(OrderStatus.DELIVERING);

        return orderRepository.save(order);
    }

    // Kuryer buyurtmani yetkazib berdi
    public Order deliverOrder(Long orderId, Long courierId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyurtma topilmadi"));

        if (order.getCourier() == null || !order.getCourier().getId().equals(courierId)) {
            throw new BadRequestException("Bu buyurtma sizga tegishli emas");
        }

        if (order.getStatus() != OrderStatus.DELIVERING) {
            throw new BadRequestException("Buyurtma yetkazilish holatida emas");
        }

        order.setStatus(OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }

    // Kuryer joylashuvini yangilash
    public Courier updateCourierLocation(Long courierId, Double latitude, Double longitude) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Kuryer topilmadi"));

        courier.setCurrentLatitude(latitude);
        courier.setCurrentLongitude(longitude);

        return courierRepository.save(courier);
    }

    // Kuryer statusini o'zgartirish (band/bo'sh)
    public Courier toggleCourierAvailability(Long courierId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Kuryer topilmadi"));

        courier.setAvailable(!courier.getAvailable());
        return courierRepository.save(courier);
    }
}