package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.RatingDto;
import org.example.tezkor.enums.*;
import org.example.tezkor.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public Rating rateShop(RatingDto dto, User user) {
        Shop shop = shopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new RuntimeException("Do'kon topilmadi"));

        Order order = null;
        if (dto.getOrderId() != null) {
            order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Buyurtma topilmadi"));
        }

        Rating rating = Rating.builder()
                .user(user)
                .shop(shop)
                .order(order)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        Rating saved = ratingRepository.save(rating);

        // Do'kon reytingini yangilash
        updateShopRating(shop);

        return saved;
    }

    @Transactional
    public Rating rateProduct(RatingDto dto, User user) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Mahsulot topilmadi"));

        Rating rating = Rating.builder()
                .user(user)
                .product(product)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        Rating saved = ratingRepository.save(rating);

        // Mahsulot reytingini yangilash
        updateProductRating(product);

        return saved;
    }

    @Transactional
    public void updateShopRating(Shop shop) {
        Double avgRating = ratingRepository.getAverageRatingForShop(shop);
        Integer totalRatings = ratingRepository.getTotalRatingsForShop(shop);

        shop.setAverageRating(avgRating != null ? avgRating : 0.0);
        shop.setTotalRatings(totalRatings != null ? totalRatings : 0);
        shopRepository.save(shop);
    }

    @Transactional
    public void updateProductRating(Product product) {
        Double avgRating = ratingRepository.getAverageRatingForProduct(product);
        Integer totalRatings = ratingRepository.getTotalRatingsForProduct(product);

        product.setAverageRating(avgRating != null ? avgRating : 0.0);
        product.setTotalRatings(totalRatings != null ? totalRatings : 0);
        productRepository.save(product);
    }

    public List<Rating> getShopRatings(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Do'kon topilmadi"));
        return ratingRepository.findByShop(shop);
    }

    public List<Rating> getProductRatings(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Mahsulot topilmadi"));
        return ratingRepository.findByProduct(product);
    }
}