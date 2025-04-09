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
import com.example.myapplication.model.Workout
import com.example.myapplication.model.WorkoutDay

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
        val daysList = mutableListOf<WorkoutDay>()

        // Create WorkoutDay objects for each day, marking every 4th day as a rest day
        val totalDays = workout?.days ?: 30 // Default to 30 if no days data
        for (i in 1..totalDays) {
            val isRest = i % 4 == 0
            daysList.add(WorkoutDay(i.toLong(), i, isRest, workout)) // Convert i to Long for dayId
        }

        // Set adapter to display days
        val adapter = DayAdapter(this, daysList)
        listView.adapter = adapter
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
