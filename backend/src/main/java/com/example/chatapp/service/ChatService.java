package com.example.chatapp.service;

import com.example.chatapp.dto.ChatResponse;
import com.example.chatapp.entity.Chat;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.mapper.AppMapper;
import com.example.chatapp.repository.ChatRepository;
import com.example.chatapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final AppMapper mapper;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository, AppMapper mapper) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ChatResponse getOrCreatePrivateChat(Long currentUserId, Long otherUserId) {
        if (currentUserId.equals(otherUserId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot create chat with yourself");
        }
        var existing = chatRepository.findPrivateChat(currentUserId, otherUserId);
        if (existing.isPresent()) {
            return mapper.toChatResponse(existing.get());
        }
        var current = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Current user not found"));
        var other = userRepository.findById(otherUserId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        var chat = new Chat();
        chat.addParticipant(current);
        chat.addParticipant(other);
        return mapper.toChatResponse(chatRepository.save(chat));
    }

    public List<ChatResponse> findMyChats(Long userId) {
        return chatRepository.findAllByUserId(userId).stream().map(mapper::toChatResponse).toList();
    }

    public ChatResponse findById(Long chatId, Long userId) {
        var chat = chatRepository.findByIdWithParticipants(chatId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Chat not found"));
        ensureParticipant(chatId, userId);
        return mapper.toChatResponse(chat);
    }

    public void ensureParticipant(Long chatId, Long userId) {
        if (!chatRepository.isParticipant(chatId, userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You are not a participant in this chat");
        }
    }
}
