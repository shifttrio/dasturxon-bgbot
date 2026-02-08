package org.example.tezkor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevenueReportDto {
    private String period; // "Kunlik", "Haftalik", "Oylik"
    private Integer orderCount;
    private Double totalRevenue;
    private Double totalDeliveryFee;
    private Double totalDiscount;
    private Double netRevenue;
    private Double workHours; // Kuryer uchun
}