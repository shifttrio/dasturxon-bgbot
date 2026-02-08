package org.example.tezkor.repository;

import org.example.tezkor.enums.PromoCode;
import org.example.tezkor.enums.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    Optional<PromoCode> findByCodeAndIsActiveTrue(String code);

    List<PromoCode> findByShopAndIsActiveTrue(Shop shop);

    List<PromoCode> findByShopIsNullAndIsActiveTrue(); // Umumiy promo kodlar

    @Query("SELECT p FROM PromoCode p WHERE p.isActive = true AND " +
            "CURRENT_TIMESTAMP BETWEEN p.startDate AND p.endDate AND " +
            "(p.usageLimit IS NULL OR p.usedCount < p.usageLimit)")
    List<PromoCode> findAllActivePromoCodes();
}