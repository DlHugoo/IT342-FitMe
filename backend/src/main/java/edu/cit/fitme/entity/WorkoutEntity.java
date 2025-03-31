package edu.cit.fitme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "workouts")
public class WorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String difficulty; // Example: Easy, Medium, Hard

    @Column(nullable = false)
    private int days;

    // Optional: future extension to total number of exercises
    // @Transient
    // private int totalExercises;

    // ==== Getters & Setters ====

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
