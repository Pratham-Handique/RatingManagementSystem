package com.pratham.RatingManagementSystem.controller;

import com.pratham.RatingManagementSystem.dto.AuthRequest;
import com.pratham.RatingManagementSystem.dto.RegisterRequest;
import com.pratham.RatingManagementSystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok("Registration successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
