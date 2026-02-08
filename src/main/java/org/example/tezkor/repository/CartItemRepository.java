package org.example.tezkor.repository;

import org.example.tezkor.enums.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Product entityga murojaat qilish uchun product.id ishlatamiz
    Optional<CartItem> findByCartIdAndProduct_Id(Long cartId, Long productId);

    List<CartItem> findByCartId(Long cartId);
}