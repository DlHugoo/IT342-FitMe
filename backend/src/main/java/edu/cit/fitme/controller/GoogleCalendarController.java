package edu.cit.fitme.controller;

import edu.cit.fitme.service.GoogleCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar-events")
public class GoogleCalendarController {

    private final GoogleCalendarService calendarService;

    public GoogleCalendarController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // ✅ GET: Health check or basic ping
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("✅ Google Calendar endpoint is up and running.");
    }

    // ✅ POST: Create a calendar event
    @PostMapping
    public ResponseEntity<String> createEvent(@RequestParam String accessToken) {
        try {
            String eventId = calendarService.createCalendarEvent(accessToken);
            return ResponseEntity.ok("✅ Event created successfully: " + eventId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Failed to create event: " + e.getMessage());
        }
    }

    // ❌ DELETE: Delete a specific calendar event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable String eventId,
                                              @RequestParam String accessToken) {
        try {
            calendarService.deleteEvent(accessToken, eventId);
            return ResponseEntity.ok("🗑️ Event deleted: " + eventId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Failed to delete event: " + e.getMessage());
        }
    }

    // 📄 GET: List events (optional)
    @GetMapping
    public ResponseEntity<String> listEvents(@RequestParam String accessToken) {
        try {
            String response = calendarService.listEvents(accessToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Failed to list events: " + e.getMessage());
        }
    }
}
