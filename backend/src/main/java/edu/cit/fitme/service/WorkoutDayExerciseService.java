package edu.cit.fitme.service;

import edu.cit.fitme.entity.WorkoutDayExerciseEntity;
import edu.cit.fitme.repository.WorkoutDayExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutDayExerciseService {

    private final WorkoutDayExerciseRepository workoutDayExerciseRepository;

    public WorkoutDayExerciseService(WorkoutDayExerciseRepository repository) {
        this.workoutDayExerciseRepository = repository;
    }

    public List<WorkoutDayExerciseEntity> getExercisesByDayId(Long dayId) {
        return workoutDayExerciseRepository.findByWorkoutDayDayId(dayId);
    }

    public Optional<WorkoutDayExerciseEntity> getById(Long id) {
        return workoutDayExerciseRepository.findById(id);
    }

    public WorkoutDayExerciseEntity create(WorkoutDayExerciseEntity entity) {
        return workoutDayExerciseRepository.save(entity);
    }

    public Optional<WorkoutDayExerciseEntity> update(Long id, WorkoutDayExerciseEntity updated) {
        return workoutDayExerciseRepository.findById(id).map(item -> {
            item.setReps(updated.getReps());
            item.setDuration(updated.getDuration());
            item.setExercise(updated.getExercise());
            return workoutDayExerciseRepository.save(item);
        });
    }

    public void delete(Long id) {
        workoutDayExerciseRepository.deleteById(id);
    }
}
