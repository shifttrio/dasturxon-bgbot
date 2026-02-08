package org.example.tezkor.controller;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.PromoCodeDto;
import org.example.tezkor.enums.PromoCode;
import org.example.tezkor.service.PromoCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promo-codes")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    // ADMIN - Promo kod yaratish
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<PromoCode> createPromoCode(@RequestBody PromoCodeDto dto) {
        return ResponseEntity.ok(promoCodeService.createPromoCode(dto));
    }

    // USER - Barcha aktiv promo kodlar
    @GetMapping("/active")
    public ResponseEntity<List<PromoCode>> getActivePromoCodes() {
        return ResponseEntity.ok(promoCodeService.getAllActivePromoCodes());
    }

    // USER - Ma'lum do'kon promo kodlari
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<PromoCode>> getShopPromoCodes(@PathVariable Long shopId) {
        return ResponseEntity.ok(promoCodeService.getPromoCodesForShop(shopId));
    }

    // USER - Promo kodni tekshirish
    @GetMapping("/validate")
    public ResponseEntity<?> validatePromoCode(
            @RequestParam String code,
            @RequestParam Long shopId,
            @RequestParam Double orderAmount
    ) {
        try {
            PromoCode promoCode = promoCodeService.validatePromoCode(code, shopId, orderAmount);
            Double discount = promoCodeService.calculateDiscount(promoCode, orderAmount);

            return ResponseEntity.ok(new PromoValidationResponse(
                    true,
                    "Promo kod to'g'ri",
                    discount,
                    promoCode
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PromoValidationResponse(false, e.getMessage(), 0.0, null));
        }
    }

    // ADMIN - Promo kodni o'chirish
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<?> deactivatePromoCode(@PathVariable Long id) {
        promoCodeService.deactivatePromoCode(id);
        return ResponseEntity.ok("Promo kod o'chirildi");
    }

    // Inner class for response
    record PromoValidationResponse(
            boolean valid,
            String message,
            Double discountAmount,
            PromoCode promoCode
    ) {}
}