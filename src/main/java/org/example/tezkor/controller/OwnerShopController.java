package org.example.tezkor.controller;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.CreateCourierRequest;
import org.example.tezkor.dto.CreateShopRequest;
import org.example.tezkor.dto.RegisterRequest;
import org.example.tezkor.enums.Courier;
import org.example.tezkor.enums.Role;
import org.example.tezkor.enums.Shop;
import org.example.tezkor.enums.User;
import org.example.tezkor.service.AuthService;
import org.example.tezkor.service.CourierService;
import org.example.tezkor.service.ShopService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/owner/shop")
@RequiredArgsConstructor
public class OwnerShopController {

    private final CourierService courierService;

    private final ShopService shopService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/create")  // consumes ni olib tashlang yoki "application/json" qiling
    @Operation(summary = "Create shop with admin")
    public Shop createShop(@RequestBody CreateShopRequest request) {  // @ModelAttribute → @RequestBody

        // 🥚 Easter Egg qismi o'zgarmaydi...

        // TEMP OWNER (keyinchalik JWT dan oling)
        User owner = new User();
        owner.setId(1L);

        // Do'kon yaratish
        Shop shop = shopService.createShop(request, owner);

        // Admin yaratish
        RegisterRequest adminRequest = new RegisterRequest();
        adminRequest.setFullname(request.getAdminFullname());
        adminRequest.setPhone(request.getAdminPhone());
        adminRequest.setPassword(request.getAdminPassword());
        adminRequest.setRole(Role.ADMIN);

        User admin = authService.registerAndReturnUser(adminRequest);

        // Adminni do'konga tayinlash
        shop = shopService.assignAdmin(shop.getId(), admin.getId());

        return shop;
    }

    @PostMapping(value = "/courier/create", consumes = "application/json")
    @Operation(
            summary = "Create new courier",
            description = "Owner creates a new independent courier with login, password and transport type. Courier can accept orders from any shop."
    )
    public Courier createCourier(@RequestBody CreateCourierRequest request) {

        System.out.println("DEBUG - Fullname: " + request.getFullname());
        System.out.println("DEBUG - Phone: " + request.getPhone());
        System.out.println("DEBUG - TransportType: " + request.getTransportType());
        // 🥚 Easter Egg: Transport turiga qarab kulgili xabarlar
        if (request.getTransportType() != null) {
            switch (request.getTransportType()) {
                case PIYODA:
                    System.out.println("🚶‍♂️ Piyoda kuryer! 10,000 qadam kuniga kafolat! 👟");
                    break;
                case ELEKTRO_SAMAKAT:
                    System.out.println("🛴 Elektro samakat! Gen Z ning tanglovi! 😄");
                    break;
                case MOTOTSIKL:
                    System.out.println("🏍️ Mototsikl! Domino's Pizza style! 🍕");
                    break;
                case MASHINA:
                    System.out.println("🚗 Mashina! VIP yetkazib berish! 🌟");
                    break;
            }
        }

        // 🥚 Easter Egg: Maxsus ismlar uchun
        if (request.getFullname() != null) {
            if (request.getFullname().toLowerCase().contains("flash")) {
                System.out.println("⚡ Flash kuryer qo'shildi! Yorug'likdan tez! 💨");
            } else if (request.getFullname().toLowerCase().contains("bolt")) {
                System.out.println("⚡ Usain Bolt! Dunyo chempioni kuryer! 🏃‍♂️");
            } else if (request.getFullname().toLowerCase().contains("freelance")) {
                System.out.println("🦅 Freelance kuryer! Erkinlik sevuvchi! 🌍");
            }
        }

        // Kuryer user'ini yaratish
        RegisterRequest userRequest = new RegisterRequest();
        userRequest.setFullname(request.getFullname());
        userRequest.setPhone(request.getPhone());
        userRequest.setPassword(request.getPassword());
        userRequest.setRole(Role.COURIER);

        User courierUser = authService.registerAndReturnUser(userRequest);

        // Kuryer yaratish (hech qaysi do'konga bog'lanmaydi)
        Courier courier = courierService.createCourier(courierUser, request);

        return courier;
    }

    @GetMapping("/my")
    public List<Shop> myShops() {
        return shopService.getOwnerShops(1L); // TEMP OWNER ID
    }
}