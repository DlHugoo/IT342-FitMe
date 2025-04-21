package com.example.myapplication.model

data class WorkoutDayExercise(
    val id: Long,
    val exerciseName: String?, // Match the backend's `exerciseName` field
    val reps: Int?,
    val duration: Int?,
    val gifUrl: String? // Add gifUrl field
)