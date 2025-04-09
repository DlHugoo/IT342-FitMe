package com.example.myapplication.model

data class WeightLog(
    val id: Long,
    val weight: Double,
    val timestamp: String, // You can parse this to a LocalDateTime if needed
    val user: User
)
