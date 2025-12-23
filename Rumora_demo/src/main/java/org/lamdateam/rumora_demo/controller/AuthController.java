package org.lamdateam.rumora_demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.lamdateam.rumora_demo.dto.AuthRequest;
import org.lamdateam.rumora_demo.dto.AuthResponse;
import org.lamdateam.rumora_demo.dto.UpdateUserRequestDto;
import org.lamdateam.rumora_demo.entity.User;
import org.lamdateam.rumora_demo.repository.IUserRepository;
import org.lamdateam.rumora_demo.security.JwtTokenProvider;
import org.lamdateam.rumora_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000, https://remjest-rumora-5a51.twc1.net")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    // === AUTHENTICATION ENDPOINTS ===

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Optional<User> userOpt = userService.getUserByUsername(request.getUsername());

            if (userOpt.isEmpty()) {
                System.out.println("User not found in DB");
                return ResponseEntity.status(401).body("User not found");
            }

            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername());
            System.out.println("User role: " + (user.getRole() != null ? user.getRole().getRoleName() : "NULL"));

            if (user.getRole() == null) {
                System.out.println("User has no role assigned");
                return ResponseEntity.status(401).body("User has no role assigned");
            }

            String inputPassword = request.getPassword();
            String storedHash = user.getPasswordHash();

            System.out.println("Input password: " + inputPassword);
            System.out.println("Stored hash: " + storedHash);
            System.out.println("Hash length: " + storedHash.length());
            System.out.println("Is BCrypt hash: " + (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")));

            boolean matches = passwordEncoder.matches(inputPassword, storedHash);
            System.out.println("Password match result: " + matches);

            if (matches) {
                System.out.println("Authentication successful!");
                String token = jwtTokenProvider.generateToken(
                        user.getUserId(),
                        user.getUsername(),
                        user.getRole().getRoleName()
                );
                System.out.println("Token generated");

                return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().getRoleName()));
            } else {
                System.out.println("Passwords do not match - returning 401");
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            System.out.println("Exception during login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            User user = userService.createUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === USER MANAGEMENT ENDPOINTS ===

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users/create")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> userData){
        try{
            String username = userData.get("username");
            String passwordHash = userData.get("password");
            User user = userService.createUser(username, passwordHash);
            return ResponseEntity.ok(user);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUser(
            @RequestBody UpdateUserRequestDto request,
            HttpServletRequest requestHttp  // ← HttpServletRequest вместо Authentication
    ) {
        try {
            // Извлекаем user_id из токена
            String authHeader = requestHttp.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String token = authHeader.substring(7);
            String userIdStr = jwtTokenProvider.getUsernameFromToken(token); // sub = user_id
            Long userId = Long.parseLong(userIdStr);

            // Обновляем профиль
            userService.updateUserById(userId, request.getUsername(), request.getPassword());
            return ResponseEntity.ok("Профиль успешно обновлён");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Неверный формат user ID");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ❌ УДАЛЁН метод /users/login — он был небезопасен и дублировал логику

    @GetMapping("/roles")
    public List<?> getAllRoles(){
        return userService.getAllRoles();
    }
}