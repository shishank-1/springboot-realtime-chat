package com.example.chatapp.controller;

import com.example.chatapp.dto.AuthResponse;
import com.example.chatapp.dto.LoginRequest;
import com.example.chatapp.dto.RegisterRequest;
import com.example.chatapp.dto.UserResponse;
import com.example.chatapp.mapper.AppMapper;
import com.example.chatapp.security.UserPrincipal;
import com.example.chatapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AppMapper mapper;

    public AuthController(AuthService authService, AppMapper mapper) {
        this.authService = authService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        return mapper.toUserResponse(((UserPrincipal) authentication.getPrincipal()).getUser());
    }
}
