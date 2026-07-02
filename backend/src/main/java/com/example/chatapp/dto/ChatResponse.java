package com.example.chatapp.dto;

import java.time.Instant;
import java.util.List;

public record ChatResponse(Long id, Instant createdAt, List<UserResponse> participants) {
}
