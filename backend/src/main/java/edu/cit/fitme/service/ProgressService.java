package edu.cit.fitme.service;

import edu.cit.fitme.entity.ProgressEntity;
import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.entity.WorkoutEntity;
import edu.cit.fitme.repository.ProgressRepository;
import edu.cit.fitme.repository.UserRepository;
import edu.cit.fitme.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    // 🆕 Log today's workout progress
    public Optional<ProgressEntity> logProgress(String email, Long workoutId) {
        return userRepository.findByEmail(email).flatMap(user -> {
            Optional<WorkoutEntity> workoutOpt = workoutRepository.findById(workoutId);

            if (workoutOpt.isEmpty()) {
                return Optional.empty(); // Workout not found
            }

            ProgressEntity progress = new ProgressEntity();
            progress.setUser(user);
            progress.setWorkout(workoutOpt.get());
            progress.setDate(LocalDate.now());

            return Optional.of(progressRepository.save(progress));
        });
    }

    // 📥 Get all logs for a user, sorted by date
    public List<ProgressEntity> getProgressByUserTimeSorted(Long userId) {
        return userRepository.findById(userId)
                .map(progressRepository::findByUserOrderByDateDesc)
                .orElse(Collections.emptyList());
    }

    // 📅 Get today's log for a user
    public Optional<ProgressEntity> getProgressForToday(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> progressRepository.findByUserAndDate(user, LocalDate.now()));
    }

    // ❌ Delete a progress log
    public void deleteProgressById(Long id) {
        progressRepository.deleteById(id);
    }
}
