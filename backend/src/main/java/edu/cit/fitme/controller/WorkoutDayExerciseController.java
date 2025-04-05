package edu.cit.fitme.controller;

import edu.cit.fitme.entity.WorkoutDayExerciseEntity;
import edu.cit.fitme.service.WorkoutDayExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/day-exercises")
public class WorkoutDayExerciseController {

    private final WorkoutDayExerciseService service;

    public WorkoutDayExerciseController(WorkoutDayExerciseService service) {
        this.service = service;
    }

    @GetMapping("/{dayId}")
    public List<WorkoutDayExerciseEntity> getByDayId(@PathVariable Long dayId) {
        return service.getExercisesByDayId(dayId);
    }

    @PostMapping
    public WorkoutDayExerciseEntity create(@RequestBody WorkoutDayExerciseEntity data) {
        return service.create(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDayExerciseEntity> update(@PathVariable Long id, @RequestBody WorkoutDayExerciseEntity data) {
        return service.update(id, data)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
