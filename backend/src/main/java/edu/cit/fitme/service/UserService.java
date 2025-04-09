package edu.cit.fitme.service;

import edu.cit.fitme.repository.UserRepository;
import edu.cit.fitme.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity createUser(UserEntity user) {
        user.setRole("user"); // 🔒 Force default role
        user.setPassword(passwordEncoder.encode(user.getPassword())); // ✅ Encode password
        return userRepository.save(user);
    }

    public boolean validatePassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

    // ✅ USER updates own profile (no role)
    public Optional<UserEntity> updateOwnProfile(String email, UserEntity updatedUser) {

        System.out.println("🔍 [SERVICE] Attempting to find user with email: " + email); // ✅ Step 2

        return userRepository.findByEmail(email).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setWeight(updatedUser.getWeight());
            user.setHeight(updatedUser.getHeight());
            user.setAge(updatedUser.getAge());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            return userRepository.save(user);
        });
    }

    // ✅ ADMIN updates any user (including role)
    public Optional<UserEntity> updateUserByAdmin(Long id, UserEntity updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setWeight(updatedUser.getWeight());
            user.setHeight(updatedUser.getHeight());
            user.setAge(updatedUser.getAge());
            user.setEmail(updatedUser.getEmail()); // if editable

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            if (updatedUser.getRole() != null && !updatedUser.getRole().isEmpty()) {
                user.setRole(updatedUser.getRole());
            }

            return userRepository.save(user);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

