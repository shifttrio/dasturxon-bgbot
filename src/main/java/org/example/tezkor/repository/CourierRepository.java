package org.example.tezkor.repository;

import org.example.tezkor.enums.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    List<Courier> findByAvailableTrue(); // Barcha mavjud kuryerlar
}