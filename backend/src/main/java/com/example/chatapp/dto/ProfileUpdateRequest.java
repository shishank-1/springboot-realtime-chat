package com.example.chatapp.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfileUpdateRequest(@NotBlank String profileImage) {
}
