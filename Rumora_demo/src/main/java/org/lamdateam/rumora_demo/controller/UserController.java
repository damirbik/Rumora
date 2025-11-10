package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.entity.User;
import org.lamdateam.rumora_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

//    @PostMapping("/users/create")
//    public ResponseEntity<?> createUser(@RequestBody Map<String, String> userData){
//        try{
//            String username = userData.get("username");
//            String passwordHash = userData.get("password");
//            //System.out.println(username + " " + passwordHash.hashCode());
//            User user = userService.createUser(username, passwordHash);
//            //System.out.println(user);
//            return ResponseEntity.ok(user);
//        } catch(Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

//    @PostMapping("/users/login")
//    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userData){
//        try {
//            String username = userData.get("username");
//            String passwordHash = userData.get("password");
//            User newUser = userService.createUser(username, passwordHash);
//            Optional<User> user = userService.getUserByUsername(username);
//            if (user.isEmpty()){ return ResponseEntity.notFound().build(); }
//            else if(user.equals(newUser)){ return ResponseEntity.ok(user); }
//            return ResponseEntity.badRequest().body("incorrect password");
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping("/roles")
    public List<?> getAllRoles(){
        return userService.getAllRoles();
    }
}
