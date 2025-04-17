package edu.cit.fitme.service;

import edu.cit.fitme.entity.WorkoutDayEntity;
import edu.cit.fitme.entity.WorkoutEntity;
import edu.cit.fitme.repository.WorkoutDayRepository;
import edu.cit.fitme.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutDayRepository workoutDayRepository;

    public WorkoutService(WorkoutRepository workoutRepository, WorkoutDayRepository workoutDayRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutDayRepository = workoutDayRepository;
    }

    public List<WorkoutEntity> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Optional<WorkoutEntity> getWorkoutById(Long id) {
        return workoutRepository.findById(id);
    }

    public WorkoutEntity createWorkout(WorkoutEntity workout) {
        // First, save the workout
        WorkoutEntity savedWorkout = workoutRepository.save(workout);

        // Then, auto-generate WorkoutDayEntity for each day
        List<WorkoutDayEntity> days = new ArrayList<>();
        for (int i = 1; i <= savedWorkout.getDays(); i++) {
            WorkoutDayEntity day = new WorkoutDayEntity();
            day.setDayNumber(i);
            day.setRestDay(false); // Default to false
            day.setWorkout(savedWorkout);
            days.add(day);
        }

        workoutDayRepository.saveAll(days);
        return savedWorkout;
    }

    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }

    public Optional<WorkoutEntity> updateWorkout(Long id, WorkoutEntity updatedWorkout) {
        return workoutRepository.findById(id).map(workout -> {
            workout.setTitle(updatedWorkout.getTitle());
            workout.setDifficulty(updatedWorkout.getDifficulty());
            workout.setDays(updatedWorkout.getDays());
            return workoutRepository.save(workout);
        });
    }
}
