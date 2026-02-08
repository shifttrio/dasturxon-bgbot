package org.example.tezkor.controller;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.AuthResponse;
import org.example.tezkor.dto.LoginRequest;
import org.example.tezkor.dto.RegisterRequest;
import org.example.tezkor.enums.User;
import org.example.tezkor.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ================== LOGIN ==================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // xatolikda ham token = null, role = null, message = e.getMessage()
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setToken(null);
            errorResponse.setRole(null);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: " + e.getMessage());
        }
    }


}
