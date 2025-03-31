package edu.cit.fitme.service;

import edu.cit.fitme.entity.WorkoutEntity;
import edu.cit.fitme.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public List<WorkoutEntity> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Optional<WorkoutEntity> getWorkoutById(Long id) {
        return workoutRepository.findById(id);
    }

    public WorkoutEntity createWorkout(WorkoutEntity workout) {
        return workoutRepository.save(workout);
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
