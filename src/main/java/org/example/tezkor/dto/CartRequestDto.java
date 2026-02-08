package org.example.tezkor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequestDto {

    private String phone;
    private Long shopId;
    private Long productId;
    private Double price;
    private Integer quantity;
}
