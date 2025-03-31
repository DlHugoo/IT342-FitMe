package edu.cit.fitme.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "exercises")
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;

    private String name;

    private String gifUrl;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<WorkoutDayExerciseEntity> usedIn;

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    // Getters and setters
}
