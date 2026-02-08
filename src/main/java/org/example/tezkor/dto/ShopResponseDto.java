package org.example.tezkor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponseDto {
    private Long shopId;
    private String shopName;
    private Double latitude;
    private Double longitude;
    private List<CategoryDto> categories;
}
