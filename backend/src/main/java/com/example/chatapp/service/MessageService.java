package com.example.chatapp.service;

import com.example.chatapp.dto.MessageRequest;
import com.example.chatapp.dto.MessageResponse;
import com.example.chatapp.entity.Message;
import com.example.chatapp.entity.MessageType;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.mapper.AppMapper;
import com.example.chatapp.repository.ChatRepository;
import com.example.chatapp.repository.MessageRepository;
import com.example.chatapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final AppMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UserRepository userRepository,
                          ChatService chatService, AppMapper mapper, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatService = chatService;
        this.mapper = mapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public MessageResponse send(Long senderId, MessageRequest request, boolean broadcast) {
        chatService.ensureParticipant(request.chatId(), senderId);
        validate(request);
        var chat = chatRepository.findById(request.chatId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Chat not found"));
        var sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Sender not found"));
        var message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setType(request.type());
        message.setText(request.text());
        message.setMediaUrl(request.mediaUrl());
        var response = mapper.toMessageResponse(messageRepository.save(message));
        if (broadcast) {
            messagingTemplate.convertAndSend("/chat/" + request.chatId(), response);
        }
        return response;
    }

    public Page<MessageResponse> history(Long chatId, Long userId, int page, int size) {
        chatService.ensureParticipant(chatId, userId);
        var pageable = PageRequest.of(page, Math.min(size, 50), Sort.by("createdAt").ascending());
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId, pageable).map(mapper::toMessageResponse);
    }

    private void validate(MessageRequest request) {
        if (request.type() == MessageType.TEXT && !StringUtils.hasText(request.text())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Text message cannot be empty");
        }
        if ((request.type() == MessageType.IMAGE || request.type() == MessageType.VIDEO) && !StringUtils.hasText(request.mediaUrl())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Media URL is required");
        }
    }
}
