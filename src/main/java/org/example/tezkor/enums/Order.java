package org.example.tezkor.enums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalAmount;
    private Double deliveryFee;
    private Double discountAmount;

    private String deliveryAddress;
    private String apartmentNumber;

    @ManyToOne
    @JoinColumn(name = "promo_code_id")
    private PromoCode promoCode;

    private Double deliveryLatitude;
    private Double deliveryLongitude;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime deliveredAt;

    private String notes;
    private String paymentMethod;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        status = OrderStatus.PENDING;
    }

    // Umumiy narxni hisoblash (items bo'yicha)
    @Transient
    public Double getTotalPrice() {
        if (items == null || items.isEmpty()) {
            return totalAmount != null ? totalAmount : 0.0;
        }
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    // Manzilni olish
    @Transient
    public String getAddress() {
        return deliveryAddress;
    }
}