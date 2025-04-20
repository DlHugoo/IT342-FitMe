package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class WorkoutDay(
    val dayId: Long,
    val dayNumber: Int,
    @SerializedName("restDay") val isRestDay: Boolean = false, // Correctly map "restDay" from JSON
    val workout: Workout?
    // val exercises: List<WorkoutDayExercise>?
)