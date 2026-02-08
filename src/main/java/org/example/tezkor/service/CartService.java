package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.enums.Cart;
import org.example.tezkor.enums.CartItem;
import org.example.tezkor.enums.CartStatus;
import org.example.tezkor.enums.Product;
import org.example.tezkor.repository.CartItemRepository;
import org.example.tezkor.repository.CartRepository;
import org.example.tezkor.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    // ACTIVE cart topiladi yoki yaratiladi
    public Cart getOrCreateCart(String phone, Long shopId) {
        return cartRepository
                .findByPhoneNumberAndShopIdAndStatus(phone, shopId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .phoneNumber(phone)
                            .shopId(shopId)
                            .status(CartStatus.ACTIVE)
                            .build();
                    return cartRepository.save(cart);
                });
    }



    // Add to cart
    public void addToCart(String phone, Long shopId, Long productId, Double price, Integer qty) {

        Cart cart = getOrCreateCart(phone, shopId);

        // Product entityni olish
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        CartItem item = cartItemRepository
                .findByCartIdAndProduct_Id(cart.getId(), productId)
                .orElse(null);

        if (item == null) {
            item = CartItem.builder()
                    .cartId(cart.getId())
                    .product(product)  // Product entityni set qilish
                    .price(price)
                    .quantity(qty)
                    .build();
        } else {
            item.setQuantity(item.getQuantity() + qty);
        }

        cartItemRepository.save(item);
    }

    // Plus minus
    public void changeQuantity(Long cartId, Long productId, Integer qty) {

        CartItem item = cartItemRepository
                .findByCartIdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (qty <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(qty);
            cartItemRepository.save(item);
        }
    }

    // Savatni olish (restaurant-wise)
    public List<CartItem> getCartItems(String phone, Long shopId) {
        // ACTIVE cartni topamiz
        return cartRepository
                .findByPhoneNumberAndShopIdAndStatus(phone, shopId, CartStatus.ACTIVE)
                // Agar cart topilsa, itemlarni qaytaradi
                .map(cart -> cartItemRepository.findByCartId(cart.getId()))
                // Agar cart bo'lmasa, bo'sh list qaytaradi
                .orElse(List.of());
    }

    // Telefon bo'yicha barcha ACTIVE cart itemlarini olish
    public List<CartItem> getAllCartItems(String phone) {
        List<Cart> carts = cartRepository.findByPhoneNumberAndStatus(phone, CartStatus.ACTIVE);

        return carts.stream()
                .flatMap(cart -> cartItemRepository.findByCartId(cart.getId()).stream())
                .toList();
    }


}