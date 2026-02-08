package org.example.tezkor.controller;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.ShopResponseDto;
import org.example.tezkor.enums.Product;
import org.example.tezkor.enums.Shop;
import org.example.tezkor.service.UserShopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserShopController {

    private final UserShopService userShopService;

    /**
     * 1️⃣ Barcha shoplarni olish (oddiy)
     */
    @GetMapping("/shops")
    public List<Shop> getAllShops() {
        return userShopService.getAllShops();
    }

    /**
     * 2️⃣ Bitta shopga tegishli productlar
     */
    @GetMapping("/shops/{shopId}/products")
    public List<Product> getProductsByShop(
            @PathVariable Long shopId) {

        return userShopService.getProductsByShop(shopId);
    }

    /**
     * 3️⃣ Barcha shop + category + product (TO‘LIQ)
     */
    @GetMapping("/shops/full")
    public List<ShopResponseDto> getAllShopsFull() {
        return userShopService.getAllShopsWithProducts();
    }

    /**
     * 4️⃣ User lokatsiyasiga eng yaqin shoplar
     */
    @GetMapping("/shops/near")
    public List<Shop> getNearestShops(
            @RequestParam Double lat,
            @RequestParam Double lng) {

        return userShopService.findNearestShops(lat, lng);
    }
    /**
     * 5️⃣ Shop ichidan bitta productni ID bilan olish
     */
    @GetMapping("/shops/{shopId}/products/{productId}")
    public Product getProductById(
            @PathVariable Long shopId,
            @PathVariable Long productId) {

        return userShopService.getProductByShopAndId(shopId, productId);
    }

}
