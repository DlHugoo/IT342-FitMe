package edu.cit.fitme.service;

import edu.cit.fitme.entity.WorkoutDayEntity;
import edu.cit.fitme.entity.WorkoutEntity;
import edu.cit.fitme.repository.WorkoutDayRepository;
import edu.cit.fitme.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutRepository workoutRepository;

    public WorkoutDayService(WorkoutDayRepository workoutDayRepository, WorkoutRepository workoutRepository) {
        this.workoutDayRepository = workoutDayRepository;
        this.workoutRepository = workoutRepository;
    }

    public List<WorkoutDayEntity> getDaysByWorkoutId(Long workoutId) {
        return workoutDayRepository.findByWorkoutWorkoutId(workoutId);
    }

    public Optional<WorkoutDayEntity> getDayById(Long id) {
        return workoutDayRepository.findById(id);
    }

    public WorkoutDayEntity createDay(WorkoutDayEntity day) {
        Long workoutId = day.getWorkout().getWorkoutId();
        WorkoutEntity fullWorkout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found with id: " + workoutId));
        day.setWorkout(fullWorkout); // ✅ attach the full workout object
        return workoutDayRepository.save(day);
    }

    public Optional<WorkoutDayEntity> updateDay(Long id, WorkoutDayEntity updatedDay) {
        return workoutDayRepository.findById(id).map(day -> {
            day.setDayNumber(updatedDay.getDayNumber());
            return workoutDayRepository.save(day);
        });
    }

    public void deleteDay(Long id) {
        workoutDayRepository.deleteById(id);
    }
}
