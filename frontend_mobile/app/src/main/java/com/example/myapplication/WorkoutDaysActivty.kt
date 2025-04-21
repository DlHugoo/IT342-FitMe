package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.WorkoutDayExerciseAdapter
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.model.WorkoutDay
import com.example.myapplication.model.WorkoutDayExercise
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.toString

class WorkoutDaysActivty : AppCompatActivity() {

    private lateinit var exerciseAdapter: WorkoutDayExerciseAdapter
    private val exercises = mutableListOf<WorkoutDayExercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout_days_activty)

        // Apply window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.workout_days)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve data passed via Intent
        val workoutDay = intent.getParcelableExtra<WorkoutDay>("workoutDay")
        val workoutDifficulty = intent.getStringExtra("workoutDifficulty") ?: "Unknown"

        if (workoutDay == null) {
            Log.e("WorkoutDaysActivty", "WorkoutDay is null. Exiting activity.")
            finish()
            return
        }

        // Set header information
        val difficultyTextView = findViewById<TextView>(R.id.tv_difficulty)
        val dayNumberTextView = findViewById<TextView>(R.id.tv_day_number)
        val workoutImageView = findViewById<ImageView>(R.id.iv_workout_image)

        difficultyTextView.text = workoutDifficulty
        dayNumberTextView.text = "Day ${workoutDay.dayNumber}"
        workoutImageView.setImageResource(getImageForDifficulty(workoutDifficulty))

        // Set up RecyclerView for exercises
        val recyclerView = findViewById<RecyclerView>(R.id.rv_exercises)
        recyclerView.layoutManager = LinearLayoutManager(this)
        exerciseAdapter = WorkoutDayExerciseAdapter(this, exercises)
        recyclerView.adapter = exerciseAdapter

        // Fetch exercises for the selected day
        fetchExercisesForDay(workoutDay.dayId)
    }

    private fun fetchExercisesForDay(dayId: Long) {
        // Retrieve the token from SharedPreferences
        val token = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("token", null)

        if (token.isNullOrEmpty()) {
            Log.e("WorkoutDaysActivty", "Token is missing. Please log in again.")
            return
        }

        val bearerToken = "Bearer $token"

        val apiService = RetrofitClient.instance
        apiService.getExercisesByDayId(dayId, bearerToken).enqueue(object : Callback<List<WorkoutDayExercise>> {
            override fun onResponse(
                call: Call<List<WorkoutDayExercise>>,
                response: Response<List<WorkoutDayExercise>>
            ) {
                if (response.isSuccessful) {
                    Log.d("WorkoutDaysActivty", "Raw JSON Response: ${response.body()}")
                    response.body()?.let { fetchedExercises ->
                        exercises.clear()
                        exercises.addAll(fetchedExercises)
                        exerciseAdapter.notifyDataSetChanged()

                        // Update the exercise count dynamically
                        val exerciseCountTextView = findViewById<TextView>(R.id.tv_exercise_count)
                        exerciseCountTextView.text = exercises.size.toString()
                    }
                } else {
                    Log.e("WorkoutDaysActivty", "Failed to fetch exercises: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<WorkoutDayExercise>>, t: Throwable) {
                Log.e("WorkoutDaysActivty", "Error fetching exercises", t)
            }
        })
    }

    private fun getImageForDifficulty(difficulty: String?): Int {
        return when (difficulty?.lowercase()) {
            "easy" -> R.drawable.easy_image
            "medium" -> R.drawable.medium_image
            "hard" -> R.drawable.hard_image
            else -> R.drawable.easy_image
        }
    }
}