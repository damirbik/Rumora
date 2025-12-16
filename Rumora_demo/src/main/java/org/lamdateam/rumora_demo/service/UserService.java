package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.entity.User;
import org.lamdateam.rumora_demo.entity.UserRole;
import org.lamdateam.rumora_demo.repository.IRoleRepository;
import org.lamdateam.rumora_demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }

        UserRole defaultRole = roleRepository.findByRoleName("User")
                .orElseThrow(() -> new RuntimeException("Default role 'User' not found"));

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, encodedPassword);
        user.setRole(defaultRole);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (userRepository.existsByUsername(username)) {
                throw new RuntimeException("Username already exists: " + username);
            }
            throw new RuntimeException("Failed to create user", e);
        }
    }

    public void updateUserById(Long userId, String newUsername, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Пользователь не найден");
        }

        User user = userOpt.get();

        if (newUsername != null && !newUsername.equals(user.getUsername())) {
            // Проверяем, что новое имя не занято
            if (userRepository.existsByUsername(newUsername)) {
                throw new RuntimeException("Имя пользователя уже занято");
            }
            user.setUsername(newUsername);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            // Хэшируем новый пароль
            user.setPasswordHash(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
    }

    public List<UserRole> getAllRoles(){
        return roleRepository.findAll();
    }
}