package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.OrderRequest;
import org.example.tezkor.enums.*;
import org.example.tezkor.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShopRepository shopRepository;

    // Zakaz berish (savatni buyurtmaga aylantirish)
    // OrderService.java ichida placeOrder metodini yangilash

    @Transactional
    public Order placeOrder(OrderRequest request) {

        Cart cart = cartRepository
                .findByPhoneNumberAndShopIdAndStatus(request.getPhoneNumber(), request.getShopId(), CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Savat topilmadi"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) throw new RuntimeException("Savat bo'sh");

        // Mahsulotlar narxi
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Restoran topilmadi"));

        // 🔥 Yetkazib berish narxini qo'shamiz
        Double deliveryFee = request.getDeliveryFee() != null ? request.getDeliveryFee() : 0.0;

        // 🔥 Umumiy narx = mahsulotlar + yetkazib berish
        double finalTotal = totalAmount + deliveryFee;

        Order order = Order.builder()
                .phoneNumber(request.getPhoneNumber())
                .shop(shop)
                .totalAmount(finalTotal) // 🔥 Umumiy narx (mahsulotlar + yetkazish)
                .deliveryFee(deliveryFee) // 🔥 Yetkazib berish narxi alohida saqlanadi
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryLatitude(request.getDeliveryLatitude())
                .deliveryLongitude(request.getDeliveryLongitude())
                .notes(request.getNotes())
                .paymentMethod(request.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .build();

        order = orderRepository.save(order);

        // Order items ni saqlash
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .price(cartItem.getPrice())
                    .quantity(cartItem.getQuantity())
                    .build();
            orderItemRepository.save(orderItem);
        }

        // Savatni tozalash
        cartItemRepository.deleteAll(cartItems);
        cart.setStatus(CartStatus.COMPLETED);
        cartRepository.save(cart);

        return order;
    }
    // Restoran buyurtmani qabul qiladi
    public Order acceptOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Buyurtma topilmadi"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Buyurtma allaqachon qabul qilingan");
        }

        order.setStatus(OrderStatus.ACCEPTED);
        order.setAcceptedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // Buyurtma statusini o'zgartirish
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Buyurtma topilmadi"));

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        return orderRepository.save(order);
    }

    // Restoran buyurtmalarini ko'rish
    public List<Order> getShopOrders(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Restoran topilmadi"));
        return orderRepository.findByShop(shop);
    }

    // Buyurtma detallari
    public Map<String, Object> getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Buyurtma topilmadi"));

        List<OrderItem> items = orderItemRepository.findByOrder(order);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("items", items);
        return response;
    }

    // KUNLIK HISOBOT
    public Map<String, Object> getDailyReport(Long shopId, LocalDate date) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Restoran topilmadi"));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findDailyOrders(shop, startOfDay, endOfDay);

        return calculateReport(orders, "Kunlik hisobot");
    }

    // HAFTALIK HISOBOT
    public Map<String, Object> getWeeklyReport(Long shopId, LocalDate startDate) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Restoran topilmadi"));

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = startDate.plusDays(7).atStartOfDay();

        List<Order> orders = orderRepository.findWeeklyOrders(shop, start, end);

        return calculateReport(orders, "Haftalik hisobot");
    }

    // OYLIK HISOBOT
    public Map<String, Object> getMonthlyReport(Long shopId, LocalDate startDate) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Restoran topilmadi"));

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = startDate.plusMonths(1).atStartOfDay();

        List<Order> orders = orderRepository.findMonthlyOrders(shop, start, end);

        return calculateReport(orders, "Oylik hisobot");
    }

    // Hisobotni hisoblash
    private Map<String, Object> calculateReport(List<Order> orders, String reportType) {
        int totalOrders = orders.size();

        double totalRevenue = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        long completedOrders = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .count();

        long cancelledOrders = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
                .count();

        Map<String, Object> report = new HashMap<>();
        report.put("reportType", reportType);
        report.put("totalOrders", totalOrders);
        report.put("completedOrders", completedOrders);
        report.put("cancelledOrders", cancelledOrders);
        report.put("totalRevenue", totalRevenue);
        report.put("orders", orders);

        return report;
    }

    // TELEFON RAQAM BO'YICHA BUYURTMALAR (TO'LIQ)
    public List<Map<String, Object>> getOrdersByPhone(String phoneNumber) {

        List<Order> orders = orderRepository.findByPhoneNumber(phoneNumber);

        if (orders.isEmpty()) {
            throw new RuntimeException("Bu telefon raqam bo'yicha buyurtmalar topilmadi");
        }

        return orders.stream().map(order -> {
            List<OrderItem> items = orderItemRepository.findByOrder(order);

            Map<String, Object> map = new HashMap<>();
            map.put("order", order);
            map.put("items", items);

            return map;
        }).toList();
    }

}