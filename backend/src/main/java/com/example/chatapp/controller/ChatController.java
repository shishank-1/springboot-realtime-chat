package com.example.chatapp.controller;

import com.example.chatapp.dto.ChatResponse;
import com.example.chatapp.dto.CreateChatRequest;
import com.example.chatapp.service.ChatService;
import com.example.chatapp.util.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final AuthUtils authUtils;

    public ChatController(ChatService chatService, AuthUtils authUtils) {
        this.chatService = chatService;
        this.authUtils = authUtils;
    }

    @PostMapping
    public ChatResponse create(@Valid @RequestBody CreateChatRequest request, Authentication authentication) {
        return chatService.getOrCreatePrivateChat(authUtils.principal(authentication).getId(), request.userId());
    }

    @GetMapping
    public List<ChatResponse> myChats(Authentication authentication) {
        return chatService.findMyChats(authUtils.principal(authentication).getId());
    }

    @GetMapping("/{id}")
    public ChatResponse byId(@PathVariable Long id, Authentication authentication) {
        return chatService.findById(id, authUtils.principal(authentication).getId());
    }
}
