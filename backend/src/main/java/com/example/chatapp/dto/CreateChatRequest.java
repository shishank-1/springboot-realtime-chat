package com.example.chatapp.dto;

import jakarta.validation.constraints.NotNull;

public record CreateChatRequest(@NotNull Long userId) {
}
