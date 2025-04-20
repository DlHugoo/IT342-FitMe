package edu.cit.fitme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "workout_day_exercises")
public class WorkoutDayExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "day_id")
    @JsonBackReference
    private WorkoutDayEntity workoutDay;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    private Integer duration; // e.g. 30 seconds
    private Integer reps; // e.g. 10 reps

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkoutDayEntity getWorkoutDay() {
        return workoutDay;
    }

    public void setWorkoutDay(WorkoutDayEntity workoutDay) {
        this.workoutDay = workoutDay;
    }

    public ExerciseEntity getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseEntity exercise) {
        this.exercise = exercise;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    // Getters and setters
}
