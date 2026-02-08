package org.example.tezkor.repository;

import org.example.tezkor.enums.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findByOwnerId(Long ownerId);

    List<Shop> findByAdminId(Long adminId); // <--- shu metod bor
    Optional<Shop> findFirstByAdminId(Long adminId);

}

