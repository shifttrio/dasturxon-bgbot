package org.example.tezkor.enums;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // masalan: "YANGI2024"

    @Column(nullable = false)
    private Integer discountPercentage; // 10, 20, 50 foiz

    private Integer discountAmount; // yoki fix summa (masalan 5000 so'm)

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    private Integer usageLimit; // necha marta ishlatish mumkin
    private Integer usedCount = 0; // necha marta ishlatilingan

    private Integer minOrderAmount; // minimal buyurtma summasi

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop; // agar ma'lum do'konga tegishli bo'lsa

    @Column(nullable = false)
    private Boolean isActive = true;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Promo kod hali amal qiladimi?
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return isActive
                && now.isAfter(startDate)
                && now.isBefore(endDate)
                && (usageLimit == null || usedCount < usageLimit);
    }
}