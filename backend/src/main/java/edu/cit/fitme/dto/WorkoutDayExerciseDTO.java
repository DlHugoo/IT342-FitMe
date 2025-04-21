package edu.cit.fitme.dto;

import edu.cit.fitme.entity.WorkoutDayExerciseEntity;

public class WorkoutDayExerciseDTO {
    private Long id;
    private String exerciseName;
    private Integer reps;
    private Integer duration;
    private String gifUrl;

    public WorkoutDayExerciseDTO(WorkoutDayExerciseEntity entity) {
        this.id = entity.getId();
        this.exerciseName = entity.getExercise() != null ? entity.getExercise().getName() : "Unknown";
        this.reps = entity.getReps();
        this.duration = entity.getDuration();
        this.gifUrl = entity.getExercise() != null ? entity.getExercise().getGifUrl() : null; // Map gifUrl
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

    public String getGifUrl() {return gifUrl;}

    public void setGifUrl(String gifUrl) {this.gifUrl = gifUrl;}
}
