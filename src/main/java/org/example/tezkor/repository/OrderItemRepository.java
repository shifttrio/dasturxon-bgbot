package org.example.tezkor.repository;

import org.example.tezkor.enums.Order;
import org.example.tezkor.enums.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}