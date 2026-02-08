package org.example.tezkor.repository;

import org.example.tezkor.enums.Order;
import org.example.tezkor.enums.OrderStatus;
import org.example.tezkor.enums.Shop;
import org.example.tezkor.enums.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Buyurtmalarni telefon raqami bo'yicha topish
    List<Order> findByPhoneNumber(String phoneNumber);

    // Restoran buyurtmalarini topish
    List<Order> findByShop(Shop shop);

    // Shop ID bo'yicha buyurtmalarni yangilaridan eskisiga tartiblangan holda olish
    List<Order> findByShopIdOrderByCreatedAtDesc(Long shopId);

    // Status bo'yicha topish
    List<Order> findByShopAndStatus(Shop shop, OrderStatus status);

    // Kuryer buyurtmalari
    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCourier(Courier courier);

    // 🔥 YANGI: Hisobotlar uchun
    List<Order> findByShopAndStatusAndCreatedAtBetween(
            Shop shop,
            OrderStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Order> findByCourierAndStatusAndDeliveredAtBetween(
            Courier courier,
            OrderStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    // Kunlik hisobot (eski)
    @Query("SELECT o FROM Order o WHERE o.shop = :shop AND o.createdAt >= :startDate AND o.createdAt < :endDate")
    List<Order> findDailyOrders(@Param("shop") Shop shop, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Haftalik hisobot (eski)
    @Query("SELECT o FROM Order o WHERE o.shop = :shop AND o.createdAt >= :startDate AND o.createdAt < :endDate")
    List<Order> findWeeklyOrders(@Param("shop") Shop shop, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Oylik hisobot (eski)
    @Query("SELECT o FROM Order o WHERE o.shop = :shop AND o.createdAt >= :startDate AND o.createdAt < :endDate")
    List<Order> findMonthlyOrders(@Param("shop") Shop shop, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}