package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.AuthResponse;
import org.example.tezkor.dto.LoginRequest;
import org.example.tezkor.dto.RegisterRequest;
import org.example.tezkor.enums.User;
import org.example.tezkor.repository.UserRepository;
import org.example.tezkor.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Parol noto‘g‘ri");
        }

        String token = jwtUtil.generateToken(user); // JWT token
        return new AuthResponse(token, user.getRole().name(), "Login muvaffaqiyatli");
    }


    public void register(RegisterRequest request) {

        User user = new User();
        user.setFullname(request.getFullname());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
    }
    public User registerAndReturnUser(RegisterRequest request) {
        User user = new User();
        user.setFullname(request.getFullname());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

}
