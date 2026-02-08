package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.CategoryDto;
import org.example.tezkor.dto.ShopResponseDto;
import org.example.tezkor.enums.Category;
import org.example.tezkor.enums.Product;
import org.example.tezkor.enums.Shop;
import org.example.tezkor.repository.CategoryRepository;
import org.example.tezkor.repository.ProductRepository;
import org.example.tezkor.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserShopService {

    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 1️⃣ Barcha shoplarni olish (oddiy)
     */
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    /**
     * 2️⃣ Bitta shopga tegishli productlar
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByShop(Long shopId) {
        return productRepository.findByShopId(shopId);
    }

    /**
     * 3️⃣ Barcha shop + category + product (TO‘LIQ)
     */
    @Transactional(readOnly = true)
    public List<ShopResponseDto> getAllShopsWithProducts() {

        List<Shop> shops = shopRepository.findAll();
        List<ShopResponseDto> response = new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();

        for (Shop shop : shops) {

            List<CategoryDto> categoryDtos = new ArrayList<>();

            for (Category category : categories) {

                List<Product> products =
                        productRepository.findByShopIdAndCategoryId(
                                shop.getId(),
                                category.getId()
                        );

                if (!products.isEmpty()) {
                    categoryDtos.add(
                            new CategoryDto(
                                    category.getId(),
                                    category.getName(),
                                    products
                            )
                    );
                }
            }

            response.add(
                    new ShopResponseDto(
                            shop.getId(),
                            shop.getName(),
                            shop.getLatitude(),
                            shop.getLongitude(),
                            categoryDtos
                    )
            );
        }

        return response;
    }

    /**
     * 4️⃣ User lokatsiyasiga eng yaqin shoplar
     */
    @Transactional(readOnly = true)
    public List<Shop> findNearestShops(Double lat, Double lng) {

        List<Shop> shops = shopRepository.findAll();

        return shops.stream()
                .filter(s -> s.getLatitude() != null && s.getLongitude() != null)
                .sorted(Comparator.comparing(
                        shop -> distance(
                                lat, lng,
                                shop.getLatitude(),
                                shop.getLongitude()
                        )
                ))
                .toList();
    }

    /**
     * Masofa hisoblash (oddiy)
     */
    private double distance(
            double lat1, double lon1,
            double lat2, double lon2) {

        return Math.sqrt(
                Math.pow(lat1 - lat2, 2) +
                        Math.pow(lon1 - lon2, 2)
        );
    }
    public Product getProductByShopAndId(Long shopId, Long productId) {
        return productRepository.findByIdAndShopId(productId, shopId);
    }

}
