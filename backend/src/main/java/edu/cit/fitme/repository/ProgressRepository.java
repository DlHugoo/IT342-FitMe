package edu.cit.fitme.repository;

import edu.cit.fitme.entity.ProgressEntity;
import edu.cit.fitme.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {

    List<ProgressEntity> findByUserOrderByDateDesc(UserEntity user);

    Optional<ProgressEntity> findByUserAndDate(UserEntity user, LocalDate date);
}
