package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.PromoCodeDto;
import org.example.tezkor.enums.PromoCode;
import org.example.tezkor.enums.Shop;
import org.example.tezkor.repository.PromoCodeRepository;
import org.example.tezkor.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;
    private final ShopRepository shopRepository;

    @Transactional
    public PromoCode createPromoCode(PromoCodeDto dto) {
        Shop shop = null;
        if (dto.getShopId() != null) {
            shop = shopRepository.findById(dto.getShopId())
                    .orElseThrow(() -> new RuntimeException("Do'kon topilmadi"));
        }

        PromoCode promoCode = PromoCode.builder()
                .code(dto.getCode().toUpperCase())
                .discountPercentage(dto.getDiscountPercentage())
                .discountAmount(dto.getDiscountAmount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .usageLimit(dto.getUsageLimit())
                .minOrderAmount(dto.getMinOrderAmount())
                .shop(shop)
                .isActive(true)
                .build();

        return promoCodeRepository.save(promoCode);
    }

    public PromoCode validatePromoCode(String code, Long shopId, Double orderAmount) {
        PromoCode promoCode = promoCodeRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("Promo kod topilmadi"));

        if (!promoCode.isValid()) {
            throw new RuntimeException("Promo kod muddati tugagan yoki limit tugadi");
        }

        // Do'kon tekshiruvi
        if (promoCode.getShop() != null && !promoCode.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Bu promo kod faqat " + promoCode.getShop().getName() + " uchun");
        }

        // Minimal summa tekshiruvi
        if (promoCode.getMinOrderAmount() != null && orderAmount < promoCode.getMinOrderAmount()) {
            throw new RuntimeException("Minimal buyurtma summasi: " + promoCode.getMinOrderAmount());
        }

        return promoCode;
    }

    @Transactional
    public void incrementUsage(PromoCode promoCode) {
        promoCode.setUsedCount(promoCode.getUsedCount() + 1);
        promoCodeRepository.save(promoCode);
    }

    public Double calculateDiscount(PromoCode promoCode, Double amount) {
        if (promoCode.getDiscountPercentage() != null) {
            return amount * promoCode.getDiscountPercentage() / 100.0;
        } else if (promoCode.getDiscountAmount() != null) {
            return Math.min(promoCode.getDiscountAmount().doubleValue(), amount);
        }
        return 0.0;
    }

    public List<PromoCode> getAllActivePromoCodes() {
        return promoCodeRepository.findAllActivePromoCodes();
    }

    public List<PromoCode> getPromoCodesForShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Do'kon topilmadi"));
        return promoCodeRepository.findByShopAndIsActiveTrue(shop);
    }

    @Transactional
    public void deactivatePromoCode(Long id) {
        PromoCode promoCode = promoCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promo kod topilmadi"));
        promoCode.setIsActive(false);
        promoCodeRepository.save(promoCode);
    }
}