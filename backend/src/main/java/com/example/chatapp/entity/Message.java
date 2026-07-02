package com.example.chatapp.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_messages_chat_created", columnList = "chat_id,created_at")
})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType type;

    @Column(columnDefinition = "TEXT")
    private String text;

    private String mediaUrl;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    public Instant getCreatedAt() { return createdAt; }
}
