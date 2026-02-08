package org.example.tezkor.enums;

import jakarta.persistence.*;
import lombok.*;
import org.example.tezkor.enums.CartStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    private Long shopId;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        status = CartStatus.ACTIVE;
    }
}