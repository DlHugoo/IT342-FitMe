package edu.cit.fitme.controller;

import edu.cit.fitme.entity.ExerciseEntity;
import edu.cit.fitme.service.CloudinaryService;
import edu.cit.fitme.service.ExerciseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final CloudinaryService cloudinaryService;

    public ExerciseController(ExerciseService exerciseService, CloudinaryService cloudinaryService) {
        this.exerciseService = exerciseService;
        this.cloudinaryService = cloudinaryService;
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

    /**
     * Upload a GIF file and return its accessible full URL.
     */
    @PostMapping("/upload-gif")
    public ResponseEntity<String> uploadGif(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

}
