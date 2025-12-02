package com.chat.app.service;

import com.chat.app.Model.Message;
import com.chat.app.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Transactional
    public Message saveMessage(String sender, String content, Message.MessageType type, String groupName) {
        Message message = new Message(sender, content, type, groupName);
        return messageRepository.save(message);
    }
    
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getRecentMessagesByGroup(String groupName, int limit) {
        // Assume you have added this method to MessageRepository
        List<Message> messages = messageRepository.findTop50ByGroupNameOrderByCreatedAtDesc(groupName);
        Collections.reverse(messages);
        return messages;
    }
    
    public List<Message> getMessagesBySender(String sender) {
        return messageRepository.findBySender(sender);
    }
    
    public List<Message> getMessagesByType(Message.MessageType type) {
        return messageRepository.findByType(type);
    }
    
    public List<Message> getMessagesBetween(LocalDateTime start, LocalDateTime end) {
        return messageRepository.findByCreatedAtBetween(start, end);
    }
}