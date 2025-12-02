package com.chat.app.repository;
import com.chat.app.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findBySender(String sender);
    
    List<Message> findByType(Message.MessageType type);
    
    List<Message> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    List<Message> findTop50ByGroupNameOrderByCreatedAtDesc(String groupName);
}