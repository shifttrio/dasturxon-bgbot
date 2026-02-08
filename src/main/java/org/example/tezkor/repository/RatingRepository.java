package org.example.tezkor.repository;

import org.example.tezkor.enums.Rating;
import org.example.tezkor.enums.Shop;
import org.example.tezkor.enums.Product;
import org.example.tezkor.enums.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByShop(Shop shop);

    List<Rating> findByProduct(Product product);

    Optional<Rating> findByUserAndShop(User user, Shop shop);

    Optional<Rating> findByUserAndProduct(User user, Product product);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.shop = :shop")
    Double getAverageRatingForShop(Shop shop);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.product = :product")
    Double getAverageRatingForProduct(Product product);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.shop = :shop")
    Integer getTotalRatingsForShop(Shop shop);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.product = :product")
    Integer getTotalRatingsForProduct(Product product);
}