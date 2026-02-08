package org.example.tezkor.repository;

import org.example.tezkor.enums.Cart;
import org.example.tezkor.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Shu methodni qo‘shamiz:
    Optional<Cart> findByPhoneNumberAndShopIdAndStatus(String phoneNumber, Long shopId, CartStatus status);
    List<Cart> findByPhoneNumberAndStatus(String phoneNumber, CartStatus status);

}
