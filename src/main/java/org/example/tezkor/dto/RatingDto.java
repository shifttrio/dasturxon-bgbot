package org.example.tezkor.dto;

import lombok.Data;

@Data
public class RatingDto {
    private Long shopId;
    private Long productId;
    private Long orderId;
    private Integer rating; // 1-5
    private String comment;
}