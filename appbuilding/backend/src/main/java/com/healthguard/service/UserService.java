package com.healthguard.service;

import com.healthguard.model.User;
import com.healthguard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(String nickname) {
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setNickname(nickname);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}