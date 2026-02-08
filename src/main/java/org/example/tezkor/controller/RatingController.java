package org.example.tezkor.controller;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.RatingDto;
import org.example.tezkor.enums.Rating;
import org.example.tezkor.enums.User;
import org.example.tezkor.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // USER - Do'kon reyting qo'yish
    @PostMapping("/shop")
    public ResponseEntity<Rating> rateShop(
            @RequestBody RatingDto dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(ratingService.rateShop(dto, user));
    }

    // USER - Mahsulot reyting qo'yish
    @PostMapping("/product")
    public ResponseEntity<Rating> rateProduct(
            @RequestBody RatingDto dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(ratingService.rateProduct(dto, user));
    }

    // USER - Do'kon reytinglarini ko'rish
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Rating>> getShopRatings(@PathVariable Long shopId) {
        return ResponseEntity.ok(ratingService.getShopRatings(shopId));
    }

    // USER - Mahsulot reytinglarini ko'rish
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductRatings(@PathVariable Long productId) {
        return ResponseEntity.ok(ratingService.getProductRatings(productId));
    }
}