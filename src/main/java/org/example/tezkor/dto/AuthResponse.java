package org.example.tezkor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token; // JWT token
    private String role;  // ADMIN, OWNER
    private String message; // xatolik yoki status
}
