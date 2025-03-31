package com.example.myapplication.model

data class User(
    val id: Long? = null,
    val username: String,
    val email: String,
    val password: String? = null,  // Optional to avoid exposing it unnecessarily
    val role: String,
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null
)
