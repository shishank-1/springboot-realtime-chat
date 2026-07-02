package com.example.chatapp.mapper;

import com.example.chatapp.dto.ChatResponse;
import com.example.chatapp.dto.MessageResponse;
import com.example.chatapp.dto.UserResponse;
import com.example.chatapp.entity.Chat;
import com.example.chatapp.entity.Message;
import com.example.chatapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AppMapper {
    public UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImage(), user.getCreatedAt());
    }

    public ChatResponse toChatResponse(Chat chat) {
        var users = chat.getParticipants().stream()
                .map(participant -> toUserResponse(participant.getUser()))
                .toList();
        return new ChatResponse(chat.getId(), chat.getCreatedAt(), users);
    }

    public MessageResponse toMessageResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getChat().getId(),
                message.getSender().getId(),
                message.getType(),
                message.getText(),
                message.getMediaUrl(),
                message.getCreatedAt()
        );
    }
}
