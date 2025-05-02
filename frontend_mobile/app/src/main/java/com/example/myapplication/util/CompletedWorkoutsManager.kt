package com.example.myapplication.util

import android.content.Context
import android.content.SharedPreferences

class CompletedWorkoutsManager(private val context: Context) {
    private val PREFS_NAME = "completed_workouts"
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    fun markWorkoutDayAsCompleted(workoutDayId: Long) {
        sharedPreferences.edit().putBoolean("workout_day_$workoutDayId", true).apply()
    }

    fun isWorkoutDayCompleted(workoutDayId: Long): Boolean {
        return sharedPreferences.getBoolean("workout_day_$workoutDayId", false)
    }

    // Add this method to your CompletedWorkoutsManager class
    fun clearAllCompletedWorkouts() {
        val editor = sharedPreferences.edit()
        editor.clear() // This will clear all preferences
        editor.apply()
    }
}