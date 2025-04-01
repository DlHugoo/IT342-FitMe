package edu.cit.fitme.repository;

import edu.cit.fitme.entity.WorkoutDayExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutDayExerciseRepository extends JpaRepository<WorkoutDayExerciseEntity, Long> {
    List<WorkoutDayExerciseEntity> findByWorkoutDayDayId(Long dayId);
}
