package com.example.myapplication.api

import com.example.myapplication.model.Exercise
import com.example.myapplication.model.User
import com.example.myapplication.model.WeightLog
import com.example.myapplication.model.Workout
import com.example.myapplication.model.WorkoutDay
import com.example.myapplication.model.WorkoutDayExercise
import okhttp3.ResponseBody
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

data class WeightUpdateRequest(
    val weight: Double
)

data class WeightLogEntity(
    val weight: Double
)

interface ApiService {

    //USER

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

    //WORKOUTS

    @GET("/api/workouts")
    fun getAllWorkouts(
        @Header("Authorization")
        token: String
    ): Call<List<Workout>>

    @GET("api/workout-days/{workoutId}")
    suspend fun getWorkoutDays(@Path("workoutId") workoutId: Long): List<WorkoutDay>

    //WEIGHT LOGS

    // ✅ Update weight (for logged-in user, uses JWT token)
    @PATCH("/api/users/weight")
    fun updateWeight(
        @Header("Authorization") token: String,
        @Body request: WeightUpdateRequest
    ): Call<User>

    @GET("/api/weights")
    fun getWeightLogs(
        @Header("Authorization") token: String
    ): Call<List<WeightLog>>

    @POST("/api/weights")
    fun logWeight(
        @Header("Authorization") token: String,
        @Body weightLog: WeightLogEntity
    ): Call<WeightLog>

    //Workout Days
    @GET("/api/workout-days")
    fun getAllWorkoutDays(
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @GET("/api/workout-days/day/{id}")
    fun getDayById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Call<WorkoutDay>


    // WORKOUT DAY EXERCISES
    // ✅ Get exercises by day ID
    @GET("/api/day-exercises/{dayId}")
    fun getExercisesByDayId(
        @Path("dayId") dayId: Long,
        @Header("Authorization") token: String
    ): Call<List<WorkoutDayExercise>>

    // EXERCISES
    // ✅ Get all exercises
    @GET("/api/exercises")
    fun getAllExercises(
        @Header("Authorization")
        token: String
    ): Call<List<Exercise>>

    // ✅ Get exercise by ID
    @GET("/api/exercises/{id}")
    fun getExerciseById(
        @Path("id") exerciseId: Exercise, // Maps "exerciseId" in the app to "id" in the backend
        @Header("Authorization") token: String
    ): Call<Exercise>
}
