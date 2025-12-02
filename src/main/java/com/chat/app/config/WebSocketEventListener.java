package com.chat.app.config;
import com.chat.app.Model.ChatMessage;
import com.chat.app.Model.Message;
import com.chat.app.service.MessageService;
import com.chat.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private  final SimpMessageSendingOperations messagingTemplate;
    private final  UserService userService;
    private final MessageService messageService;

    WebSocketEventListener(SimpMessageSendingOperations messagingTemplate,UserService userService,MessageService messageService){
        this.messagingTemplate=messagingTemplate;
        this.userService=userService;
        this.messageService=messageService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String groupName = (String) headerAccessor.getSessionAttributes().get("groupName");

        if(username != null && groupName != null) {
            logger.info("User Disconnected : " + username + " from group: " + groupName);

            userService.removeUser(username);

            messageService.saveMessage(
                    username,
                    username + " left " + groupName + "!",
                    Message.MessageType.LEAVE,
                    groupName
            );

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

            messagingTemplate.convertAndSend("/topic/" + groupName, chatMessage);
        }
    }
}