package org.example.tezkor.enums;

public enum OrderStatus {
    PENDING,      // Yangi buyurtma
    ACCEPTED,     // Restoran qabul qildi
    PREPARING,    // Tayyorlanmoqda
    READY,        // Tayyor
    DELIVERING,   // Yetkazilmoqda
    DELIVERED,    // Yetkazildi
    CANCELLED     // Bekor qilindi
}