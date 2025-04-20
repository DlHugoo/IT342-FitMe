package edu.cit.fitme.service;

import edu.cit.fitme.entity.ProgressEntity;
import edu.cit.fitme.entity.WorkoutDayEntity;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleCalendarService {

    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ Create a simple placeholder event
    public String createCalendarEvent(String accessToken) {
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> event = new HashMap<>();
        event.put("summary", "FitMe Workout Reminder");
        event.put("description", "Your scheduled workout!");

        Map<String, String> start = new HashMap<>();
        start.put("dateTime", "2025-04-25T09:00:00+08:00");
        start.put("timeZone", "Asia/Manila");
        event.put("start", start);

        Map<String, String> end = new HashMap<>();
        end.put("dateTime", "2025-04-25T10:00:00+08:00");
        end.put("timeZone", "Asia/Manila");
        event.put("end", end);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(event, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }

    // ✅ Create an event for a specific workout day
    public String createCalendarEventForWorkoutDay(String accessToken, WorkoutDayEntity day) {
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        LocalDate date = day.getScheduledDate(); // assumes field exists
        String isoStart = date.atTime(9, 0) + ":00+08:00";
        String isoEnd = date.atTime(10, 0) + ":00+08:00";

        Map<String, Object> event = new HashMap<>();
        event.put("summary", "Workout Day " + day.getDayNumber());
        event.put("description", day.isRestDay() ? "Rest Day 💤" : "Workout Exercises Scheduled 💪");

        Map<String, String> start = new HashMap<>();
        start.put("dateTime", isoStart);
        start.put("timeZone", "Asia/Manila");
        event.put("start", start);

        Map<String, String> end = new HashMap<>();
        end.put("dateTime", isoEnd);
        end.put("timeZone", "Asia/Manila");
        event.put("end", end);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(event, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("id");
        }

        throw new RuntimeException("Failed to create Google Calendar event");
    }

    // ✅ Create an event for a progress log
    public String createCalendarEventForProgress(String accessToken, ProgressEntity progress) {
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        LocalDate date = progress.getDate();
        String startTime = date.atTime(7, 0) + ":00+08:00";
        String endTime = date.atTime(8, 0) + ":00+08:00";

        Map<String, Object> event = new HashMap<>();
        event.put("summary", "✅ Completed Workout: " + progress.getWorkout().getTitle());
        event.put("description", "Logged via FitMe App");

        Map<String, String> start = new HashMap<>();
        start.put("dateTime", startTime);
        start.put("timeZone", "Asia/Manila");
        event.put("start", start);

        Map<String, String> end = new HashMap<>();
        end.put("dateTime", endTime);
        end.put("timeZone", "Asia/Manila");
        event.put("end", end);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(event, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("id");
        }

        throw new RuntimeException("Failed to create calendar event for progress");
    }

    // ❌ Delete event
    public void deleteEvent(String accessToken, String eventId) {
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events/" + eventId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
    }

    // 📋 List events
    public String listEvents(String accessToken) {
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events?maxResults=5&orderBy=startTime&singleEvents=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return response.getBody();
    }
}
