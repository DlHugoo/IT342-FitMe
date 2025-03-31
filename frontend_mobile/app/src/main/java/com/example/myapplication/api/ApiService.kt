package com.example.myapplication.api

import com.example.myapplication.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // Get user by ID
    @GET("/api/users/getUserById/{id}")
    fun getUserById(@Path("id") id: Long): Call<User>

    // Create a new user
    @POST("/api/users/createUser")
    fun createUser(@Body user: User): Call<User>

    // Update a user by ID
    @PUT("/api/users/updateUser/{id}")
    fun updateUser(@Path("id") id: Long, @Body user: User): Call<User>

    // Delete a user by ID
    @DELETE("/api/users/deleteUser/{id}")
    fun deleteUser(@Path("id") id: Long): Call<Void>

    // Encode a password
    @GET("/api/users/encode/{rawPassword}")
    fun encodePassword(@Path("rawPassword") rawPassword: String): Call<String>
}
