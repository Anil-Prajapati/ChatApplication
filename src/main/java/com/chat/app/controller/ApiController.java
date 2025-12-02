package com.chat.app.controller;

import com.chat.app.Model.Message;
import com.chat.app.Model.User;
import com.chat.app.service.MessageService;
import com.chat.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
    
    @GetMapping("/messages/recent")
    public ResponseEntity<List<Message>> getRecentMessages(@RequestParam(defaultValue = "50") int limit,@RequestParam String groupName) {
        return ResponseEntity.ok(messageService.getRecentMessagesByGroup(groupName, limit));
    }
    
    @GetMapping("/messages/user/{username}")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable String username) {
        return ResponseEntity.ok(messageService.getMessagesBySender(username));
    }
    
    @GetMapping("/users/online")
    public ResponseEntity<List<User>> getOnlineUsers() {
        return ResponseEntity.ok(userService.getOnlineUsers());
    }
    
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}