package com.example.chatapp.websocket;

import com.example.chatapp.dto.MessageRequest;
import com.example.chatapp.dto.MessageResponse;
import com.example.chatapp.security.UserPrincipal;
import com.example.chatapp.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    private final MessageService messageService;

    public ChatWebSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat.send")
    public MessageResponse send(@Valid MessageRequest request, Authentication authentication) {
        var principal = (UserPrincipal) authentication.getPrincipal();
        return messageService.send(principal.getId(), request, true);
    }
}
