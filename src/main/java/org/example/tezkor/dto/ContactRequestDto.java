package org.example.tezkor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequestDto {
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String chatId; // Telegram chat id
}
