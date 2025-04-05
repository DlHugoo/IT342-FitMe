package com.example.myapplication.api

import com.example.myapplication.model.User
import com.example.myapplication.model.Workout
import retrofit2.Call
import retrofit2.http.*

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: Long,
    val email: String,
    val role: String
)

interface ApiService {

    // ✅ Login (still AuthController, unchanged)
    @POST("/api/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    // ✅ Get all users (Admin only)
    @GET("/api/users")
    fun getAllUsers(
        @Header("Authorization") token: String
    ): Call<List<User>>

    // ✅ Get user by ID
    @GET("/api/users/{id}")
    fun getUserById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Call<User>

    // ✅ Create new user
    @POST("/api/users")
    fun createUser(@Body user: User): Call<User>

    // ✅ Users update their own profile (email extracted from JWT on server side)
    @PUT("/api/users")
    fun updateOwnProfile(
        @Header("Authorization") token: String,
        @Body user: User
    ): Call<User>

    // ✅ Admin updates a specific user by ID
    @PUT("/api/users/{id}")
    fun updateUserByAdmin(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body user: User
    ): Call<User>

    // ✅ Delete user by ID
    @DELETE("/api/users/{id}")
    fun deleteUser(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Call<Void>

    // ✅ Encode password (for testing/admin use)
    @GET("/api/users/encode/{rawPassword}")
    fun encodePassword(
        @Path("rawPassword") rawPassword: String
    ): Call<String>

    @GET("/api/workouts")
    fun getAllWorkouts( @Header("Authorization") token: String): Call<List<Workout>>

}
