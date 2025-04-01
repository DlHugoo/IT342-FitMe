package edu.cit.fitme.repository;

import edu.cit.fitme.entity.WorkoutDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutDayRepository extends JpaRepository<WorkoutDayEntity, Long> {
    List<WorkoutDayEntity> findByWorkoutWorkoutId(Long workoutId);
}
