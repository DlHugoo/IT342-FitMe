package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.model.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageActivity : AppCompatActivity() {
    private lateinit var workoutContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homepage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Profile navigation
        val navprofile: LinearLayout = findViewById(R.id.nav_profile)
        navprofile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Find container where workout cards will be added
        workoutContainer = findViewById(R.id.workout_container)

        // Fetch workouts from backend
        loadWorkouts()
    }

    private fun loadWorkouts() {
        // Get token from SharedPreferences
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Token not found, please login again", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val authHeader = "Bearer $token"  // Add "Bearer " prefix to the token

        RetrofitClient.instance.getAllWorkouts(authHeader).enqueue(object : Callback<List<Workout>> {
            override fun onResponse(call: Call<List<Workout>>, response: Response<List<Workout>>) {
                if (response.isSuccessful) {
                    val workouts = response.body() ?: emptyList()
                    val difficulties = workouts.map { it.difficulty }.distinct()

                    // Log for debugging purposes
                    Log.d("HomePageActivity", "Fetched workouts: ${workouts.size} workouts")

                    // Iterate through unique difficulties and create cards for each difficulty
                    val workoutsByDifficulty = workouts.groupBy { it.difficulty }

                    workouts.forEach { workout ->
                        val cardView = LayoutInflater.from(this@HomePageActivity)
                            .inflate(R.layout.item_workout_category, workoutContainer, false)

                        // Find views inside the card layout
                        val difficultyTextView = cardView.findViewById<TextView>(R.id.difficulty_title)
                        val descTextView = cardView.findViewById<TextView>(R.id.difficulty_desc)
                        val imageView = cardView.findViewById<ImageView>(R.id.difficulty_image)

                        // Set values dynamically
                        difficultyTextView.text = workout.difficulty
                        descTextView.text = workout.title
                        imageView.setImageResource(getImageForDifficulty(workout.difficulty))

                        // Add the workout card to the container
                        workoutContainer.addView(cardView)
                    }
                } else {
                    // Enhanced error handling
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.e("HomePageActivity", "Failed to load workouts, response code: ${response.code()}, error: $errorBody")
                        Toast.makeText(this@HomePageActivity, "Failed to load workouts (${response.code()})", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e("HomePageActivity", "Error parsing error response", e)
                        Toast.makeText(this@HomePageActivity, "Failed to load workouts", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Workout>>, t: Throwable) {
                // Handle failure (e.g., network error)
                Log.e("HomePageActivity", "Failed to load workouts: ${t.message}", t)
                Toast.makeText(this@HomePageActivity, "Failed to load workouts: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getImageForDifficulty(difficulty: String): Int {
        return when (difficulty.lowercase()) {
            "easy" -> R.drawable.easy_image // Ensure these image resources exist in the 'res/drawable' folder
            "medium" -> R.drawable.medium_image
            "hard" -> R.drawable.hard_image
            else -> R.drawable.easy_image // Provide a fallback image if difficulty is unknown
        }
    }
}
