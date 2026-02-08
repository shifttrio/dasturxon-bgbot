package org.example.tezkor.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String phoneNumber;
    private Long shopId;
    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    private Double deliveryFee; // 🔥 Yetkazib berish narxi
    private String notes;
    private String paymentMethod; // CASH, CARD
}