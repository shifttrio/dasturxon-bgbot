package org.example.tezkor.enums;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    private int price;
    private double weight;

    private int quantity = 0;

    // internet rasm link
    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    // ✅ tayyor bo'lish vaqti (minutda)
    @Column(name = "prepare_time")
    private Integer prepareTime;

    // 🔥 FEATURED/TRENDING
    private Boolean isFeatured = false;  // Aylanib turuvchi mahsulot
    private Boolean isActive = true;     // Mahsulot faolmi

    // 🔥 REYTING
    private Double averageRating = 0.0; // O'rtacha reyting
    private Integer totalRatings = 0;   // Reyting soni

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}