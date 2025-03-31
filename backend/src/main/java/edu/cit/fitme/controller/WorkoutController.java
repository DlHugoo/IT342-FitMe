package edu.cit.fitme.controller;

import edu.cit.fitme.entity.WorkoutEntity;
import edu.cit.fitme.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    public List<WorkoutEntity> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutEntity> getWorkoutById(@PathVariable Long id) {
        return workoutService.getWorkoutById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public WorkoutEntity createWorkout(@RequestBody WorkoutEntity workout) {
        return workoutService.createWorkout(workout);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<WorkoutEntity> updateWorkout(@PathVariable Long id, @RequestBody WorkoutEntity workout) {
        return workoutService.updateWorkout(id, workout)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }
}
