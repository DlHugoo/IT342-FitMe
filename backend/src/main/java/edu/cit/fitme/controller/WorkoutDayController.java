package edu.cit.fitme.controller;

import edu.cit.fitme.entity.WorkoutDayEntity;
import edu.cit.fitme.entity.WorkoutEntity;
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

    @GetMapping
    public List<WorkoutDayEntity> getAllWorkoutDays() {
        return workoutDayService.getAllDays(); // <- make sure this method exists
    }

    @GetMapping("/{workoutId}")
    public List<WorkoutDayEntity> getByWorkoutId(@PathVariable Long workoutId) {
        return workoutDayService.getDaysByWorkoutId(workoutId);
    }

    @PostMapping
    public WorkoutDayEntity create(@RequestBody WorkoutDayEntity day) {
        return workoutDayService.createDay(day);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDayEntity> update(@PathVariable Long id, @RequestBody WorkoutDayEntity day) {
        return workoutDayService.updateDay(id, day)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workoutDayService.deleteDay(id);
        return ResponseEntity.noContent().build();
    }
}
