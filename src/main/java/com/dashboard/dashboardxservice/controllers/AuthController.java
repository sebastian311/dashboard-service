package com.dashboard.dashboardxservice.controllers;

import com.dashboard.dashboardxservice.models.User;
import com.dashboard.dashboardxservice.repository.UserRepository;
import com.dashboard.dashboardxservice.services.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "isAdmin", user.get().isAdmin()
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid username or password"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Successfully logged out"));
    }
}