package edu.cit.fitme.repository;

import edu.cit.fitme.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
    // you can add custom queries later if needed
}
