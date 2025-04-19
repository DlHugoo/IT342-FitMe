package edu.cit.fitme.dto;

import edu.cit.fitme.entity.WorkoutDayExerciseEntity;

public class WorkoutDayExerciseDTO {
    private Long id;
    private String exerciseName;
    private Integer reps;
    private Integer duration;

    public WorkoutDayExerciseDTO(WorkoutDayExerciseEntity entity) {
        this.id = entity.getId();
        this.exerciseName = entity.getExercise() != null ? entity.getExercise().getName() : "Unknown";
        this.reps = entity.getReps();
        this.duration = entity.getDuration();
    }

    public Long getId() {
        return id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public Integer getReps() {
        return reps;
    }

    public Integer getDuration() {
        return duration;
    }
}
