package com.example.myapplication.model

data class WorkoutDay(
    val dayId: Long,
    val dayNumber: Int,
    val isRestDay: Boolean,
    val workout: Workout?, // optional depending on use
    //val exercises: List<WorkoutDayExercise>?
)
