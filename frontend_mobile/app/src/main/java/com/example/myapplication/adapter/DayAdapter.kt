package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.model.WorkoutDay
import com.example.myapplication.util.CompletedWorkoutsManager

class DayAdapter(
    context: Context,
    private val days: List<WorkoutDay>
) : ArrayAdapter<WorkoutDay>(context, 0, days) {

    private val completedWorkoutsManager = CompletedWorkoutsManager(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_day, parent, false)

        val day = days[position]

        val restText = view.findViewById<TextView>(R.id.tvRest)
        
        val tvDay = view.findViewById<TextView>(R.id.tvDay)
        val tvRest = view.findViewById<TextView>(R.id.tvRest)
        val checkIcon = view.findViewById<ImageView>(R.id.checkIcon)
        
        tvDay.text = "Day ${day.dayNumber}"
        
        if (day.isRestDay) {
            tvRest.visibility = View.VISIBLE
            restText.text = "REST"
            checkIcon.visibility = View.GONE
        } else {
            tvRest.visibility = View.GONE
        }
        
        // Check if this workout day is completed
        if (completedWorkoutsManager.isWorkoutDayCompleted(day.dayId)) {
            // Set the check icon to blue
            checkIcon.setColorFilter(context.resources.getColor(R.color.blue))
        } else {
            // Reset to default white color
            checkIcon.clearColorFilter()
        }
        
        return view
    }
}


