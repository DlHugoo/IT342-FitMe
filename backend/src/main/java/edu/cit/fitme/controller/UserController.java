package edu.cit.fitme.controller;

import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Accessible by ADMIN only (secured via SecurityConfig)
    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userService.createUser(user);
    }

    // ✅ USERS edit own profile (email taken from token)
    @PutMapping
    public ResponseEntity<UserEntity> updateOwnProfile(@RequestBody UserEntity updatedUser, Principal principal) {
        // Cast back to UserEntity from security context
        UserEntity loggedInUser = (UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        String email = loggedInUser.getEmail();

        System.out.println("[CONTROLLER] Logged-in user email from JWT: " + email);

        return userService.updateOwnProfile(email, updatedUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ ADMIN edits any user including role
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUserByAdmin(@PathVariable Long id, @RequestBody UserEntity updatedUser) {
        return userService.updateUserByAdmin(id, updatedUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/encode/{rawPassword}")
    public String encodePassword(@PathVariable String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}


