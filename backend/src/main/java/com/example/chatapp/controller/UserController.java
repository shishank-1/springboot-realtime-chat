package com.example.chatapp.controller;

import com.example.chatapp.dto.ProfileUpdateRequest;
import com.example.chatapp.dto.UserResponse;
import com.example.chatapp.service.UserService;
import com.example.chatapp.util.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthUtils authUtils;

    public UserController(UserService userService, AuthUtils authUtils) {
        this.userService = userService;
        this.authUtils = authUtils;
    }

    @GetMapping
    public List<UserResponse> all(Authentication authentication) {
        return userService.allUsers(authUtils.principal(authentication).getId());
    }

    @GetMapping("/search")
    public List<UserResponse> search(@RequestParam String q, Authentication authentication) {
        return userService.search(q, authUtils.principal(authentication).getId());
    }

    @GetMapping("/{id}")
    public UserResponse byId(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/profile")
    public UserResponse updateProfile(@Valid @RequestBody ProfileUpdateRequest request, Authentication authentication) {
        return userService.updateProfile(authUtils.principal(authentication).getId(), request);
    }
}
