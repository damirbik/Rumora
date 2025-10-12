package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.entity.User;
import org.lamdateam.rumora_demo.entity.UserRole;
import org.lamdateam.rumora_demo.repository.IRoleRepository;
import org.lamdateam.rumora_demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(String username, String email, String passwordHash, String roleName){
        if(userRepository.existsByUsername(username)){
            throw new RuntimeException("Username already exists: " + username);

        }
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Email already exists: " + email);
        }

        UserRole role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User user = new User(username, email, passwordHash, role);
        return userRepository.save(user);
    }

    public List<UserRole> getAllRoles(){
        return roleRepository.findAll();
    }

    public Optional<UserRole> getRoleByName(String roleName){
        return roleRepository.findByRoleName(roleName);
    }
}
