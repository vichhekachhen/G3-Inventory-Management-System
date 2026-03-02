package com.example.inventory_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.inventory_management_system.entities.User;
import com.example.inventory_management_system.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users with pagination
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findByEnabledTrue(pageable);
    }

    // Find user by ID only if enabled = true
    public User findById(Long id) {
        return userRepository.findByUserIdAndEnabled(id, true)
                .orElseThrow(() -> new RuntimeException("Active user not found with id " + id));
    }

    // Save user
    public void save(User user) {
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    //Update user
    public void update(Long id, User updatedUser) {
        User existing = findById(id);

        existing.setUsername(updatedUser.getUsername());
        existing.setRole(updatedUser.getRole());
        existing.setEnabled(updatedUser.isEnabled());

        // Only update password if provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existing);
    }

    //Soft-delete user
    public void disableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Set enabled to false (0 in DB)
        user.setEnabled(false);

        userRepository.save(user);
    }
}
