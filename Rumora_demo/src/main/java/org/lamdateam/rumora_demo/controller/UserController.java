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
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

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
            String email = userData.get("email");
            String passwordHash = userData.get("password");
//            String roleName; //= userData.get("roleName");
            System.out.println(username + " " + email + " " + passwordHash);
            User user = userService.createUser(username, email, passwordHash);
            System.out.println(user);
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
