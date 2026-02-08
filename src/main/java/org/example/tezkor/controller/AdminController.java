package org.example.tezkor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.CreateProductRequest;
import org.example.tezkor.enums.*;
import org.example.tezkor.repository.UserRepository;
import org.example.tezkor.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;

    // ✅ Admin o'z shoplarini ko'radi
    @GetMapping("/shops")
    @Operation(summary = "Get my shops")
    public List<Shop> getMyShops() {
        User admin = getCurrentAdmin();
        return adminService.getAdminShops(admin.getId());
    }

    // ✅ Shop active / inactive qilish
    @PutMapping("/shop/{shopId}/active")
    @Operation(summary = "Activate or deactivate shop")
    public Map<String, String> updateShopActiveStatus(
            @PathVariable Long shopId,
            @RequestParam boolean active
    ) {
        User admin = getCurrentAdmin();

        adminService.updateShopActiveStatus(shopId, active, admin.getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", active ? "Shop activated" : "Shop deactivated");

        return response;
    }


    // ✅ Shop buyurtmalarini olish
    @GetMapping("/orders/{shopId}")
    @Operation(summary = "Get shop orders")
    public List<Order> getShopOrders(@PathVariable Long shopId) {
        User admin = getCurrentAdmin();
        return adminService.getShopOrders(shopId, admin.getId());
    }

    // ✅ Buyurtma statusini o'zgartirish
    @PutMapping("/order/{orderId}/status")
    @Operation(summary = "Update order status")
    public Map<String, String> updateOrderStatus(@PathVariable Long orderId,
                                                 @RequestParam OrderStatus status) {
        User admin = getCurrentAdmin();
        adminService.updateOrderStatus(orderId, status, admin.getId());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Status yangilandi");
        return response;
    }

    // ✅ Product active / inactive qilish
    @PutMapping("/product/{productId}/active")
    @Operation(summary = "Activate or deactivate product")
    public Map<String, String> updateProductActiveStatus(
            @PathVariable Long productId,
            @RequestParam boolean active
    ) {
        User admin = getCurrentAdmin();

        adminService.updateProductActiveStatus(productId, active, admin.getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", active ? "Product activated" : "Product deactivated");

        return response;
    }

    // ✅ Mahsulotni o'chirish
    @DeleteMapping("/product/{productId}")
    @Operation(summary = "Delete product")
    public Map<String, String> deleteProduct(@PathVariable Long productId) {
        User admin = getCurrentAdmin();
        adminService.deleteProduct(productId, admin.getId());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Mahsulot o'chirildi");
        return response;
    }

    // ✅ Product qo'shish (image + category bilan)
    @PostMapping(value = "/product", consumes = {"multipart/form-data"})
    @Operation(
            summary = "Add product to shop",
            description = "Admin uploads a product with image",
            requestBody = @RequestBody(content = @Content(mediaType = "multipart/form-data"))
    )
    public Product addProduct(@ModelAttribute CreateProductRequest request) {
        User admin = getCurrentAdmin();
        return adminService.addProductToShop(request, admin.getId());
    }

    // ✅ Barcha productlar
    @GetMapping("/products")
    @Operation(summary = "Get all products")
    public List<Product> getAllProducts() {
        return adminService.getAllProducts();
    }

    // ✅ Category yaratish
    @PostMapping("/category")
    @Operation(summary = "Create category")
    public Category createCategory(@RequestParam String name) {
        return adminService.createCategory(name);
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories")
    public List<Category> getAllCategories() {
        return adminService.getAllCategories();
    }


    // 🔐 Hozirgi adminni JWT orqali olish
    private User getCurrentAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String phone = (String) auth.getPrincipal();

        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }
}