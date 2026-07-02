package com.example.chatapp.controller;

import com.example.chatapp.dto.MessageRequest;
import com.example.chatapp.dto.MessageResponse;
import com.example.chatapp.service.MessageService;
import com.example.chatapp.util.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    private final AuthUtils authUtils;

    public MessageController(MessageService messageService, AuthUtils authUtils) {
        this.messageService = messageService;
        this.authUtils = authUtils;
    }

    @PostMapping
    public MessageResponse send(@Valid @RequestBody MessageRequest request, Authentication authentication) {
        return messageService.send(authUtils.principal(authentication).getId(), request, true);
    }

    @GetMapping("/{chatId}")
    public Page<MessageResponse> history(@PathVariable Long chatId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "30") int size,
                                         Authentication authentication) {
        return messageService.history(chatId, authUtils.principal(authentication).getId(), page, size);
    }
}
