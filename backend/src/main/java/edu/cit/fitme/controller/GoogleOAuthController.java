package edu.cit.fitme.controller;

import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.repository.UserRepository;
import edu.cit.fitme.service.GoogleCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/google")
public class GoogleOAuthController {

    private final UserRepository userRepository;
    private final GoogleCalendarService calendarService;

    public GoogleOAuthController(UserRepository userRepository, GoogleCalendarService calendarService) {
        this.userRepository = userRepository;
        this.calendarService = calendarService;
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
}
