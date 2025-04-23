package edu.cit.fitme.controller;

import edu.cit.fitme.dto.ProgressRequest;
import edu.cit.fitme.entity.ProgressEntity;
import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    // 🆕 Log today's workout with token
    @PostMapping
    public ResponseEntity<ProgressEntity> logProgress(
            @RequestBody ProgressRequest request,
            @RequestHeader("X-Access-Token") String accessToken,
            Principal principal) {

        UserEntity user = (UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return progressService.logProgress(user.getEmail(), request.getWorkoutId(), accessToken)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 📥 Get all logs (latest first)
    @GetMapping
    public ResponseEntity<List<ProgressEntity>> getProgressLogs(Principal principal) {
        UserEntity user = (UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(progressService.getProgressByUserTimeSorted(user.getId()));
    }

    // 📅 Get today's progress
    @GetMapping("/today")
    public ResponseEntity<ProgressEntity> getTodayProgress(Principal principal) {
        UserEntity user = (UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return progressService.getProgressForToday(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // ❌ Delete progress (also from calendar)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id,
                                               @RequestHeader("X-Access-Token") String accessToken) {
        progressService.deleteProgressById(id, accessToken);
        return ResponseEntity.noContent().build();
    }
}
