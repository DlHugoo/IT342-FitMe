package edu.cit.fitme.service;

import edu.cit.fitme.entity.ExerciseEntity;
import edu.cit.fitme.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<ExerciseEntity> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public Optional<ExerciseEntity> getById(Long id) {
        return exerciseRepository.findById(id);
    }

    public ExerciseEntity createExercise(ExerciseEntity exercise) {
        return exerciseRepository.save(exercise);
    }

    public Optional<ExerciseEntity> updateExercise(Long id, ExerciseEntity updatedExercise) {
        return exerciseRepository.findById(id).map(exercise -> {
            exercise.setName(updatedExercise.getName());
            exercise.setGifUrl(updatedExercise.getGifUrl());
            return exerciseRepository.save(exercise);
        });
    }

    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }


}
