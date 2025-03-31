package edu.cit.fitme.controller;

import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/createUser")
    public UserEntity createUser(@RequestBody UserEntity user) {

        return userService.createUser(user);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<UserEntity> updateOwnProfile(@RequestBody UserEntity updatedUser, Principal principal) {
        String email = principal.getName(); // Automatically pulled from token
        return userService.updateOwnProfile(email, updatedUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/encode/{rawPassword}")
    public String encodePassword(@PathVariable String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}

