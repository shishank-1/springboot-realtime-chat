package com.example.chatapp.dto;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String email,
        String profileImage,
        Instant createdAt
) {
}
