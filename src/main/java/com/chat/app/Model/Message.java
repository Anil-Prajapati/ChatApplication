
package com.chat.app.Model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Entity
@Table(name = "messages")
@Setter
@Getter
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String groupName;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public Message() {
        this.createdAt = LocalDateTime.now();
    }

    public Message(String sender, String content, MessageType type, String groupName) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.groupName = groupName; // Set group name
        this.createdAt = LocalDateTime.now();
    }

    }