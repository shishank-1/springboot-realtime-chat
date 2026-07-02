package com.example.chatapp.dto;

import com.example.chatapp.entity.MessageType;
import java.time.Instant;

public record MessageResponse(
        Long id,
        Long chatId,
        Long senderId,
        MessageType type,
        String text,
        String mediaUrl,
        Instant createdAt
) {
}
