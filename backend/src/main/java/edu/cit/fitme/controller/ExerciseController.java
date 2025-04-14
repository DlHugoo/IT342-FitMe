package edu.cit.fitme.controller;

import edu.cit.fitme.entity.ExerciseEntity;
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

    /**
     * Upload a GIF file and return its accessible full URL.
     */
    @PostMapping("/upload-gif")
    public ResponseEntity<String> uploadGif(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // Set up the directory
            String uploadDir = "uploads/gifs";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generate a unique filename
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Save the file
            Path filePath = Paths.get(uploadDir).resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            // Construct full file URL
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String fileUrl = baseUrl + "/uploads/gifs/" + uniqueFileName;

            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }
}
