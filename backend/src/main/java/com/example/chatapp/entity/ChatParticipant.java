package com.example.chatapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_participants", uniqueConstraints = {
        @UniqueConstraint(name = "uk_chat_user", columnNames = {"chat_id", "user_id"})
})
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    protected ChatParticipant() {
    }

    public ChatParticipant(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
    }

    public Long getId() { return id; }
    public Chat getChat() { return chat; }
    public User getUser() { return user; }
}
