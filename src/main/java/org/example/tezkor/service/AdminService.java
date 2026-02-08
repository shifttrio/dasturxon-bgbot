package org.example.tezkor.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.CreateProductRequest;
import org.example.tezkor.enums.*;
import org.example.tezkor.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // Admin o'z shoplarini oladi
    public List<Shop> getAdminShops(Long adminId) {
        return shopRepository.findByAdminId(adminId);
    }
    public void updateProductActiveStatus(Long productId, boolean active, Long adminId) {

        Product product = productRepository
                .findByIdAndShop_Admin_Id(productId, adminId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(active);

        productRepository.save(product);
    }

    public void updateShopActiveStatus(Long shopId, boolean active, Long adminId) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        // xavfsizlik: faqat o‘z shopini o‘zgartira oladi
        if (!shop.getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("You are not owner of this shop");
        }

        shop.setActive(active);

        shopRepository.save(shop);
    }

    // Shop buyurtmalarini olish
    public List<Order> getShopOrders(Long shopId, Long adminId) {
        // Admin ushbu shopga tegishli ekanligini tekshirish
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (!shop.getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Access denied: You don't have access to this shop");
        }

        return orderRepository.findByShopIdOrderByCreatedAtDesc(shopId);
    }

    // Buyurtma statusini o'zgartirish
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus, Long adminId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Admin ushbu shopga tegishli ekanligini tekshirish
        if (!order.getShop().getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Access denied: You don't have access to this order");
        }

        order.setStatus(newStatus);

        // Status o'zgarganda vaqtlarni yangilash
        if (newStatus == OrderStatus.ACCEPTED) {
            order.setAcceptedAt(LocalDateTime.now());
        } else if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        orderRepository.save(order);
    }

    // Mahsulotni o'chirish
    @Transactional
    public void deleteProduct(Long productId, Long adminId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Admin ushbu shopga tegishli ekanligini tekshirish
        if (!product.getShop().getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Access denied: You don't have access to this product");
        }

        productRepository.delete(product);
    }

    @Transactional
    public Product addProductToShop(CreateProductRequest request, Long adminId) {

        Shop shop = shopRepository.findFirstByAdminId(adminId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setWeight(request.getWeight());
        product.setPrepareTime(request.getPrepareTime());

        product.setImageUrl(request.getImageUrl());
        product.setShop(shop);
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Transactional
    public Category createCategory(String name) {
        categoryRepository.findByName(name).ifPresent(c -> {
            throw new RuntimeException("Category already exists");
        });

        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Barcha productlarni olish
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    private Shop getAdminShop(Long adminId) {
        return shopRepository.findByAdminId(adminId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Admin has no shop"));
    }
}