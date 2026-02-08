package org.example.tezkor.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                // REST API uchun CSRF o'chiriladi
                .csrf(AbstractHttpConfigurer::disable)

                // CORS enable
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Session ishlatmaymiz (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization
                .authorizeHttpRequests(auth -> auth

                        // 🔓 PUBLIC endpoints
                        .requestMatchers(
                                "/",                  // root endpoint
                                "/error",             // Whitelabel error fallback
                                "/auth/**",           // login/register
                                "/user/**",           // public user endpoints
                                "/api/contacts/**",
                                "/api/cart/**",
                                "/api/orders/place",
                                "/api/cart/all",
                                "/api/orders/by-phone", // 👈 shu endpointni public qildik


                                // Swagger/OpenAPI
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "localhost:3000/**",
                                "localhost:3001/**"
                        ).permitAll()

                        // 🔐 ROLE BASED endpoints
                        .requestMatchers("/owner/**").hasRole("OWNER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/orders/**").hasRole("ADMIN")
                        .requestMatchers("/api/courier/**").hasRole("COURIER")

                        // 🔒 Qolgan barcha so'rovlar authentication talab qiladi
                        .anyRequest().authenticated()
                )


                // JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // PRODUCTION + DEVELOPMENT domains
        config.setAllowedOriginPatterns(List.of(

                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:3001",

                // Railway frontend
                "https://*.railway.app",

                // Netlify
                "https://*.netlify.app",

                // ngrok
                "https://*.ngrok-free.app"

        ));

        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));

        config.setAllowCredentials(true);

        config.setExposedHeaders(List.of(
                "Authorization"
        ));

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
