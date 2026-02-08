package org.example.tezkor.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromoCodeDto {
    private String code;
    private Integer discountPercentage;
    private Integer discountAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer usageLimit;
    private Integer minOrderAmount;
    private Long shopId; // null bo'lsa umumiy promo kod
}