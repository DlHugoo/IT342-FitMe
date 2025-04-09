package edu.cit.fitme.service;

import edu.cit.fitme.entity.UserEntity;
import edu.cit.fitme.entity.WeightLogEntity;
import edu.cit.fitme.repository.UserRepository;
import edu.cit.fitme.repository.WeightLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WeightLogService {

    @Autowired
    private WeightLogRepository weightLogRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Log a new weight for a user
    public Optional<WeightLogEntity> logWeight(Long userId, double weight) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            WeightLogEntity log = new WeightLogEntity();
            log.setUser(userOpt.get());
            log.setWeight(weight);
            log.setTimestamp(LocalDateTime.now());

            return Optional.of(weightLogRepository.save(log));
        }

        return Optional.empty();
    }

    // ✅ Get all weight logs for a user sorted by time (latest first)
    public List<WeightLogEntity> getLogsByUserTimeSorted(Long userId) {
        return userRepository.findById(userId)
                .map(user -> weightLogRepository.findByUserOrderByTimestampDesc(user))
                .orElse(List.of());
    }

    // ✅ Get all weight logs for a user sorted by weight (highest first)
    public List<WeightLogEntity> getLogsByUserWeightSorted(Long userId) {
        return userRepository.findById(userId)
                .map(user -> weightLogRepository.findByUserOrderByWeightDesc(user))
                .orElse(List.of());
    }

    // ✅ Optional: delete a weight log by ID
    public void deleteWeightLog(Long id) {
        weightLogRepository.deleteById(id);
    }
}
