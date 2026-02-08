package org.example.tezkor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {

    private String name;
    private String description;
    private int price;
    private double weight;

    // ✅ tayyor bo‘lish vaqti (minut)
    private Integer prepareTime;

    // category nomi bilan tanlaydi
    private String categoryName;

    // image link (yoki multipartdan olinadi)
    private String imageUrl;
}
