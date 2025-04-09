package edu.cit.fitme.controller;

import edu.cit.fitme.entity.ExerciseEntity;
import edu.cit.fitme.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public List<ExerciseEntity> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseEntity> getById(@PathVariable Long id) {
        return exerciseService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ExerciseEntity create(@RequestBody ExerciseEntity exercise) {
        return exerciseService.createExercise(exercise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseEntity> update(@PathVariable Long id, @RequestBody ExerciseEntity exercise) {
        return exerciseService.updateExercise(id, exercise)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
