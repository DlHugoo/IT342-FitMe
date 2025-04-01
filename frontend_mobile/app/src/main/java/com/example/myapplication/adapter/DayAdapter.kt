package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.model.WorkoutDays

class DayAdapter(private val context: Context, private val days: List<WorkoutDays>) :
    ArrayAdapter<WorkoutDays>(context, 0, days) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_day, parent, false)

        val dayText = view.findViewById<TextView>(R.id.tvDay)
        val restText = view.findViewById<TextView>(R.id.tvRest)
        val checkIcon = view.findViewById<ImageView>(R.id.checkIcon)

        val workoutDays = days[position]

        // Set the day number
        dayText.text = "Day ${workoutDays.dayNumber}"

        if (workoutDays.isRestDay) {
            // Mark Rest Days
            restText.visibility = View.VISIBLE
            restText.text = "Rest"
            checkIcon.visibility = View.GONE
        } else {
            restText.visibility = View.GONE
            checkIcon.visibility = View.VISIBLE
        }

        return view
    }
}
