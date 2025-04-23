package edu.cit.fitme.controller;

import edu.cit.fitme.entity.WorkoutDayEntity;
import edu.cit.fitme.service.WorkoutDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-days")
public class WorkoutDayController {

    private final WorkoutDayService workoutDayService;

    public WorkoutDayController(WorkoutDayService workoutDayService) {
        this.workoutDayService = workoutDayService;
    }

    // 📥 GET all workout days
    @GetMapping
    public List<WorkoutDayEntity> getAllWorkoutDays() {
        return workoutDayService.getAllDays();
    }

    // 📥 GET all days by workoutId
    @GetMapping("/workout/{workoutId}")
    public List<WorkoutDayEntity> getByWorkoutId(@PathVariable Long workoutId) {
        return workoutDayService.getDaysByWorkoutId(workoutId);
    }

    // 📅 GET single day by ID
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDayEntity> getDayById(@PathVariable Long id) {
        return workoutDayService.getDayById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST create workout day + sync to calendar
    @PostMapping
    public ResponseEntity<WorkoutDayEntity> createDay(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody WorkoutDayEntity day
    ) {
        String token = extractToken(authHeader);
        WorkoutDayEntity created = workoutDayService.createDay(day, token);
        return ResponseEntity.ok(created);
    }

    // ✏️ PUT update workout day
    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDayEntity> update(
            @PathVariable Long id,
            @RequestBody WorkoutDayEntity day
    ) {
        return workoutDayService.updateDay(id, day)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🗑 DELETE workout day (and calendar event)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = extractToken(authHeader);
        workoutDayService.deleteDay(id, token);
        return ResponseEntity.noContent().build();
    }

    // 🔁 Manually sync calendar
    @PostMapping("/{id}/sync-calendar")
    public ResponseEntity<WorkoutDayEntity> syncToCalendar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = extractToken(authHeader);
        return workoutDayService.syncWorkoutDayToCalendar(id, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🧠 Helper: Extract Bearer token
    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
