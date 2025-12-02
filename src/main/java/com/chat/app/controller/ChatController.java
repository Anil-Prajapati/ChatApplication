package com.chat.app.controller;

import com.chat.app.Model.ChatMessage;
import com.chat.app.Model.Message;
import com.chat.app.service.MessageService;
import com.chat.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
    public void sendMessage(@Payload ChatMessage chatMessage,
                            SimpMessageHeaderAccessor headerAccessor) {

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String groupName = (String) headerAccessor.getSessionAttributes().get("groupName"); // 🔥 New

        if (username != null && groupName != null) {
            chatMessage.setSender(username);
            chatMessage.setTimestamp(getCurrentTimestamp());

            messageService.saveMessage(
                    username,
                    chatMessage.getContent(),
                    Message.MessageType.valueOf(chatMessage.getType().name()),
                    groupName // 🔥 Save with Group
            );

            // Send to the specific group topic
            messagingTemplate.convertAndSend("/topic/" + groupName, chatMessage);
        }
    }


    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {

        String groupName = chatMessage.getContent(); // Client will send groupName in content field

        // Add username and groupName in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("groupName", groupName); // 🔥 Store Group Name

        chatMessage.setTimestamp(getCurrentTimestamp());

        userService.addUser(chatMessage.getSender());

        // Save JOIN message to database (Group name is required now)
        messageService.saveMessage(
                chatMessage.getSender(),
                chatMessage.getSender() + " joined " + groupName + "!",
                Message.MessageType.JOIN,
                groupName // 🔥 Save with Group
        );
        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setType(ChatMessage.MessageType.JOIN);
        joinMessage.setSender(chatMessage.getSender());
        joinMessage.setTimestamp(getCurrentTimestamp());
        messagingTemplate.convertAndSend("/topic/" + groupName, joinMessage);
        // Return the message for all group members to see
        return chatMessage;
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
