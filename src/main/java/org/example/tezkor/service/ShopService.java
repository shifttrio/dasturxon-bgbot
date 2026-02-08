package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.CreateShopRequest;
import org.example.tezkor.enums.Shop;
import org.example.tezkor.enums.User;
import org.example.tezkor.repository.ShopRepository;
import org.example.tezkor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public Shop createShop(CreateShopRequest request, User owner) {
        Shop shop = new Shop();
        shop.setName(request.getName());
        shop.setAddress(request.getAddress());
        shop.setDescription(request.getDescription());
        shop.setEstimatedDeliveryTime(request.getEstimatedDeliveryTime());
        shop.setOwner(owner);

        // 🗺️ Lokatsiya saqlash
        shop.setLatitude(request.getLatitude());
        shop.setLongitude(request.getLongitude());

        // 🔥 Rasm URL bilan saqlash
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            shop.setImage(request.getImageUrl());
        } else {
            shop.setImage("img/premium.jpg"); // default rasm
        }

        return shopRepository.save(shop);
    }


    public Shop assignAdmin(Long shopId, Long adminId) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        shop.setAdmin(admin);
        return shopRepository.save(shop);
    }

    public List<Shop> getOwnerShops(Long ownerId) {
        return shopRepository.findByOwnerId(ownerId);
    }

}