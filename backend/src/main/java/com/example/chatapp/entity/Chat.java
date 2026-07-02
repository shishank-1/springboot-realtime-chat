package com.example.chatapp.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatParticipant> participants = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public void addParticipant(User user) {
        ChatParticipant participant = new ChatParticipant(this, user);
        participants.add(participant);
    }

    public Long getId() { return id; }
    public Set<ChatParticipant> getParticipants() { return participants; }
    public Instant getCreatedAt() { return createdAt; }
}
