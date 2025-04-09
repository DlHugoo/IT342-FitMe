package edu.cit.fitme.controller;

import edu.cit.fitme.entity.WeightLogEntity;
import edu.cit.fitme.service.WeightLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/weights")
public class WeightLogController {

    @Autowired
    private WeightLogService weightLogService;

    // 🆕 POST weight entry (authenticated user)
    @PostMapping
    public ResponseEntity<WeightLogEntity> logWeight(@RequestBody WeightLogEntity request, Principal principal) {
        var user = (edu.cit.fitme.entity.UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return weightLogService.logWeight(user.getId(), request.getWeight())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    // 📥 GET all weight logs (latest first)
    @GetMapping
    public ResponseEntity<List<WeightLogEntity>> getAllLogs(Principal principal) {
        var user = (edu.cit.fitme.entity.UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(weightLogService.getLogsByUserTimeSorted(user.getId()));
    }

    // ❌ DELETE a weight log by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeightLog(@PathVariable Long id) {
        weightLogService.deleteWeightLog(id);
        return ResponseEntity.noContent().build();
    }
}
