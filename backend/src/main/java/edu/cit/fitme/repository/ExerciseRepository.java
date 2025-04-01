package edu.cit.fitme.repository;

import edu.cit.fitme.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
    boolean existsByName(String name); // for checking duplicates if needed
}
