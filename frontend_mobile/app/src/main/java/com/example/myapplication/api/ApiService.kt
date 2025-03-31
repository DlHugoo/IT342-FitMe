package com.example.myapplication.api

import com.example.myapplication.model.User
import retrofit2.Call
import retrofit2.http.*

data class LoginRequest(
    val email: String,   // Must match backend DTO
    val password: String // Must match backend DTO
)

data class LoginResponse(
    val token: String,    // JWT token
    val userId: Long,     // User ID
    val email: String,    // User's email
    val role: String      // User's role ('admin' or 'user')
)

interface ApiService {

    // Get user by ID (Include Authorization header)
    @GET("/api/users/getUserById/{id}")
    fun getUserById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Call<User>

    @POST("/api/auth/login") // Correct endpoint for AuthController
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    // Create a new user
    @POST("/api/users/createUser")
    fun createUser(@Body user: User): Call<User>

    @PUT("/api/users/updateProfile")
    fun updateOwnProfile(
        @Header("Authorization") token: String,  // Add token as header
        @Body user: User
    ): Call<User>

    // Delete a user by ID
    @DELETE("/api/users/deleteUser/{id}")
    fun deleteUser(@Path("id") id: Long): Call<Void>

    // Encode a password
    @GET("/api/users/encode/{rawPassword}")
    fun encodePassword(@Path("rawPassword") rawPassword: String): Call<String>

}
