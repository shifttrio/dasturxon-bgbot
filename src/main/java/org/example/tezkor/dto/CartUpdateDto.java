package org.example.tezkor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartUpdateDto {

    private Long cartId;
    private Long productId;
    private Integer quantity;
}
