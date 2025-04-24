package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.adapter.DayAdapter
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.model.Workout
import com.example.myapplication.model.WorkoutDay
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.text.get

class WorkoutsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workouts)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.workouts)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navhome: LinearLayout = findViewById(R.id.nav_home)
        navhome.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        val navstats: LinearLayout = findViewById(R.id.nav_stats)
        navstats.setOnClickListener {
            val intent = Intent(this, StatsPageActivity::class.java)
            startActivity(intent)
        }

        // Profile navigation
        val navprofile: LinearLayout = findViewById(R.id.nav_profile)
        navprofile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Retrieve data from the Intent
        val workoutDayId = intent.getLongExtra("workoutDayId", -1)
        val workoutDayNumber = intent.getIntExtra("workoutDayNumber", -1)
        val workoutDifficulty = intent.getStringExtra("workoutDifficulty")

        // Use the data to update the UI
        if (workoutDayId != -1L && workoutDayNumber != -1) {
            // Update UI elements with the retrieved data
            findViewById<TextView>(R.id.difficulty_title).text = "Day $workoutDayNumber"
            findViewById<TextView>(R.id.difficulty_desc).text = workoutDifficulty
        }

        // Retrieve workout details passed via Intent
        val workout = intent.getParcelableExtra<Workout>("workout")

        // Update the workout details in the UI
        val workoutId: Long = workout?.workoutId ?: 0L
        val workoutTitle = findViewById<TextView>(R.id.difficulty_title)
        val workoutDesc = findViewById<TextView>(R.id.difficulty_desc)
        val workoutImage = findViewById<ImageView>(R.id.difficulty_image)

        workoutTitle.text = workout?.difficulty ?: "Workout Title"
        workoutDesc.text = workout?.title ?: "Workout Description"
        workoutImage.setImageResource(getImageForDifficulty(workout?.difficulty))

        // Generate days dynamically
        val listView = findViewById<ListView>(R.id.listViewDays)

        // Fetch workout days from API
        val apiService = RetrofitClient.instance
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""

        if (token.isNotEmpty()) {
            refreshWorkoutDays(workoutId, token)
        }
    }

    private fun refreshWorkoutDays(workoutId: Long, token: String) {
        val apiService = RetrofitClient.instance
        apiService.getAllWorkoutDays("Bearer $token").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Log the raw JSON response
                    val rawJson = response.body()?.string()
                    android.util.Log.d("WorkoutsActivity", "Raw JSON Response: $rawJson")

                    // Parse the JSON response into a list of WorkoutDay objects
                    val gson = RetrofitClient.gson
                    val workoutDays = gson.fromJson(rawJson, Array<WorkoutDay>::class.java).toList()

                    // Filter and display the workout days
                    val daysList = workoutDays.filter { it.workout?.workoutId == workoutId }
                    daysList.forEach { day ->
                        android.util.Log.d("WorkoutsActivity", "Day ${day.dayNumber}, isRestDay: ${day.isRestDay}")
                    }

                    val listView = findViewById<ListView>(R.id.listViewDays)
                    val adapter = DayAdapter(this@WorkoutsActivity, daysList)
                    listView.adapter = adapter

                    // Set an OnClickListener for each item
                    listView.setOnItemClickListener { _, _, position, _ ->
                        val selectedDay = daysList[position]
                        if (selectedDay.isRestDay) {
                            // Redirect to RestDayActivity if it's a rest day
                            val intent = Intent(this@WorkoutsActivity, RestDayActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Redirect to WorkoutDaysActivty if it's not a rest day
                            val intent = Intent(this@WorkoutsActivity, WorkoutDaysActivty::class.java)
                            intent.putExtra("workoutDay", selectedDay)
                            intent.putExtra("workoutDifficulty", selectedDay.workout?.difficulty)
                            startActivity(intent)
                        }
                    }
                } else {
                    // Log the error response
                    val errorJson = response.errorBody()?.string()
                    android.util.Log.e("WorkoutsActivity", "Error Response: $errorJson")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
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