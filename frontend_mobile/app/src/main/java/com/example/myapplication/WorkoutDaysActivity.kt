package com.example.myapplication

import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.adapter.DayAdapter
import com.example.myapplication.model.WorkoutDays

class WorkoutDaysActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout_days)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.workoutDays)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.listViewDays)

        // Generate 30 days and mark every 4th day as a rest day
        val daysList = mutableListOf<WorkoutDays>()
        for (i in 1..30) {
            val isRest = i % 4 == 0
            daysList.add(WorkoutDays(i, isRest))
        }

        // Set adapter
        val adapter = DayAdapter(this, daysList)
        listView.adapter = adapter
    }
}