package com.chat.app.service;


import com.chat.app.Model.User;
import com.chat.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public User addUser(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setIsOnline(true);
            user.setLastSeen(LocalDateTime.now());
            return userRepository.save(user);
        } else {
            User newUser = new User(username);
            return userRepository.save(newUser);
        }
    }
    
    @Transactional
    public void removeUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User u = user.get();
            u.setIsOnline(false);
            u.setLastSeen(LocalDateTime.now());
            userRepository.save(u);
        }
    }
    
    public List<User> getOnlineUsers() {
        return userRepository.findByIsOnline(true);
    }
    
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}