package edu.cit.fitme.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.repository.UserRepository;
import edu.cit.fitme.security.JwtUtil;
import edu.cit.fitme.service.GoogleCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/google")
public class GoogleOAuthController {

    private final UserRepository userRepository;
    private final GoogleCalendarService calendarService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static final String MOBILE_CLIENT_ID = "450145433567-bj3u9j4377b6c76jtfpmk6gb4l5ncvbn.apps.googleusercontent.com";

    public GoogleOAuthController(UserRepository userRepository,
                                 GoogleCalendarService calendarService,
                                 JwtUtil jwtUtil,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.calendarService = calendarService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Save token (frontend sends token after manual OAuth)
    @PostMapping("/token")
    public ResponseEntity<?> saveAccessToken(@RequestParam String token, Principal principal) {
        UserEntity user = (UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        user.setGoogleAccessToken(token);
        userRepository.save(user);
        return ResponseEntity.ok("Token saved successfully");
    }

    // ✅ Get token (if needed)
    @GetMapping("/token")
    public ResponseEntity<String> getAccessToken(Principal principal) {
        UserEntity user = (UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(user.getGoogleAccessToken());
    }

    // ✅ View upcoming events (uses stored token)
    @GetMapping("/calendar/events")
    public ResponseEntity<String> listEvents(Principal principal) {
        UserEntity user = (UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        String token = user.getGoogleAccessToken();
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("No Google token found");
        }
        String eventsJson = calendarService.listEvents(token);
        return ResponseEntity.ok(eventsJson);
    }

    // ✅ Mobile login via Google ID token (for Android/iOS)
    @PostMapping("/mobile-login")
    public ResponseEntity<?> loginFromMobile(@RequestParam String token) {
        GoogleIdToken.Payload payload = verifyGoogleToken(token);
        if (payload == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid Google ID token"));
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        UserEntity user = userRepository.findByEmail(email).orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setPassword(passwordEncoder.encode("oauth2-login")); // dummy
            newUser.setRole("user");
            newUser.setGoogleConnected(true);
            return userRepository.save(newUser);
        });

        if (user.getGoogleConnected() == null || !user.getGoogleConnected()) {
            user.setGoogleConnected(true);
            userRepository.save(user);
        }

        String jwt = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    // ✅ Verify Google ID token
    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(MOBILE_CLIENT_ID)) // ✅ Secure check
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            return idToken != null ? idToken.getPayload() : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
