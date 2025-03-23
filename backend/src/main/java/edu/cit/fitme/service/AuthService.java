package edu.cit.fitme.service;

import edu.cit.fitme.dto.LoginRequest;
import edu.cit.fitme.dto.LoginResponse;
import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.repository.UserRepository;
import edu.cit.fitme.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.validatePassword(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(token, user.getId(), user.getEmail());
    }
}