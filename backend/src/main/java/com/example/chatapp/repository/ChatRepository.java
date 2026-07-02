package com.example.chatapp.repository;

import com.example.chatapp.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("""
            select distinct c from Chat c
            join fetch c.participants participants
            join fetch participants.user
            join c.participants p1
            join c.participants p2
            where p1.user.id = :firstUserId and p2.user.id = :secondUserId
            """)
    Optional<Chat> findPrivateChat(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);

    @Query("select distinct c from Chat c join fetch c.participants p join fetch p.user where c.id in (select cp.chat.id from ChatParticipant cp where cp.user.id = :userId)")
    List<Chat> findAllByUserId(@Param("userId") Long userId);

    @Query("select distinct c from Chat c join fetch c.participants p join fetch p.user where c.id = :chatId")
    Optional<Chat> findByIdWithParticipants(@Param("chatId") Long chatId);

    @Query("select count(cp) > 0 from ChatParticipant cp where cp.chat.id = :chatId and cp.user.id = :userId")
    boolean isParticipant(@Param("chatId") Long chatId, @Param("userId") Long userId);
}
