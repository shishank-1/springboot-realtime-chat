package com.example.chatapp.dto;

import com.example.chatapp.entity.MessageType;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
        @NotNull Long chatId,
        @NotNull MessageType type,
        String text,
        String mediaUrl
) {
}
