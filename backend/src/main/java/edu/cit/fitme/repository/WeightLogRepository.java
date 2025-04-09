package edu.cit.fitme.repository;

import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.entity.WeightLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightLogRepository extends JpaRepository<WeightLogEntity, Long> {

    List<WeightLogEntity> findByUserOrderByTimestampDesc(UserEntity user); // 👈 define this
    List<WeightLogEntity> findByUserOrderByWeightDesc(UserEntity user);
}
