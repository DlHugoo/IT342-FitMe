package edu.cit.fitme.entity;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "workout_days")
public class WorkoutDayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayId;

    private int dayNumber;

    @Column(name = "is_rest_day")
    private boolean isRestDay; // ✅ Added rest day flag

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    @OneToMany(mappedBy = "workoutDay", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<WorkoutDayExerciseEntity> exercises;

    // ====== Getters and Setters ======

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public boolean isRestDay() {
        return isRestDay;
    }

    public void setRestDay(boolean restDay) {
        isRestDay = restDay;
    }

    public WorkoutEntity getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutEntity workout) {
        this.workout = workout;
    }

    public List<WorkoutDayExerciseEntity> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutDayExerciseEntity> exercises) {
        this.exercises = exercises;
    }
}
