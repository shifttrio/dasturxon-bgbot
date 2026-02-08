package org.example.tezkor.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactResponseDto {
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String chatId; // Telegram chat id
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isNewContact;


}
