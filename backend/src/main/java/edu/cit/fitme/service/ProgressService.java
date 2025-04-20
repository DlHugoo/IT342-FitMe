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

    @Autowired
    private GoogleCalendarService googleCalendarService;

    // ✅ Log progress + optional Google Calendar sync
    public Optional<ProgressEntity> logProgress(String email, Long workoutId, String accessToken) {
        return userRepository.findByEmail(email).flatMap(user -> {
            Optional<WorkoutEntity> workoutOpt = workoutRepository.findById(workoutId);

            if (workoutOpt.isEmpty()) {
                return Optional.empty(); // Workout not found
            }

            WorkoutEntity workout = workoutOpt.get();

            ProgressEntity progress = new ProgressEntity();
            progress.setUser(user);
            progress.setWorkout(workout);
            progress.setDate(LocalDate.now());

            // Optional calendar sync
            try {
                String calendarEventId = googleCalendarService.createCalendarEventForProgress(accessToken, progress);
                progress.setCalendarEventId(calendarEventId);
            } catch (Exception e) {
                System.err.println("⚠️ Calendar sync failed: " + e.getMessage());
            }

            return Optional.of(progressRepository.save(progress));
        });
    }

    // 📥 Get all progress logs
    public List<ProgressEntity> getProgressByUserTimeSorted(Long userId) {
        return userRepository.findById(userId)
                .map(progressRepository::findByUserOrderByDateDesc)
                .orElse(Collections.emptyList());
    }

    // 📅 Get today's progress (if any)
    public Optional<ProgressEntity> getProgressForToday(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> progressRepository.findByUserAndDate(user, LocalDate.now()));
    }

    // ❌ Delete progress log and calendar event if exists
    public void deleteProgressById(Long id, String accessToken) {
        progressRepository.findById(id).ifPresent(progress -> {
            if (progress.getCalendarEventId() != null) {
                try {
                    googleCalendarService.deleteEvent(accessToken, progress.getCalendarEventId());
                } catch (Exception e) {
                    System.err.println("⚠️ Calendar event deletion failed: " + e.getMessage());
                }
            }

            progressRepository.deleteById(id);
        });
    }
}
