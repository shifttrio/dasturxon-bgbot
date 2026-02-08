package org.example.tezkor.enums;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shops")
@Getter
@Setter
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String estimatedDeliveryTime;

    @Column(name = "image")
    private String image; // URL

    // 🔥 LOKATSIYA
    private Double latitude;   // masalan: 41.2995
    private Double longitude;  // masalan: 69.2401

    // 🔥 DASTAVKA NARXI
    private Double deliveryFeePerKm = 2000.0; // 1 km uchun narx (so'mda)
    private Double baseDeliveryFee = 5000.0;  // Minimal dastavka narxi
    private Double freeDeliveryAbove;         // Qancha summadan yuqori bo'lsa bepul (masalan 100000)

    // 🔥 REYTING
    private Double averageRating = 0.0; // O'rtacha reyting (0-5)
    private Integer totalRatings = 0;   // Jami reyting soni

    // 🔥 FEATURED/TRENDING
    private Boolean isFeatured = false;  // Aylanib turuvchi mahsulotlar uchun
    private Boolean isActive = true;     // Do'kon faolmi

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}