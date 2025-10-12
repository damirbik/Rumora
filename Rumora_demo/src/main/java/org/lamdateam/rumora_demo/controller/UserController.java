package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.entity.User;
import org.lamdateam.rumora_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> userData){
        try{
            String username = userData.get("username");
            String email = userData.get("email");
            String passwordHash = userData.get("passwordHash");
            String roleName = userData.get("roleName");

            User user = userService.createUser(username, email, passwordHash, roleName);
            return ResponseEntity.ok(user);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/roles")
    public List<?> getAllRoles(){
        return userService.getAllRoles();
    }
}
