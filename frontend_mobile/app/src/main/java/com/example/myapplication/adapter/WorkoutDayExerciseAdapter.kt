package com.example.myapplication.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myapplication.R
import com.example.myapplication.model.WorkoutDayExercise

class WorkoutDayExerciseAdapter(
    private val context: Context,
    private val exercises: List<WorkoutDayExercise>
) : RecyclerView.Adapter<WorkoutDayExerciseAdapter.ExerciseViewHolder>() {

    private val BASE_URL = "https://metal-cable-458215-n5.as.r.appspot.com"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val workoutDayExercise = exercises[position]

        // Bind exercise name
        holder.exerciseName.text = workoutDayExercise.exerciseName ?: "Unknown"

        // Display reps or duration
        val reps = workoutDayExercise.reps ?: 0
        val duration = workoutDayExercise.duration ?: 0
        holder.exerciseTime.text = when {
            reps > 0 -> "$reps repetitions"
            duration > 0 -> "$duration seconds"
            else -> "N/A"
        }

        // Prepend base URL to gifUrl if it's a relative path
        val fullGifUrl = if (workoutDayExercise.gifUrl?.startsWith("/") == true) {
            BASE_URL + workoutDayExercise.gifUrl
        } else {
            workoutDayExercise.gifUrl
        }
        Log.d("WorkoutDayExerciseAdapter", "Loading URL: $fullGifUrl")

        // Load gifUrl dynamically or use placeholder
        Glide.with(context)
            .load(fullGifUrl)
            .placeholder(R.drawable.exercise_sample)
            .error(R.drawable.exercise_sample)
            .into(holder.exerciseImage)
    }

    override fun getItemCount(): Int = exercises.size

    // Define the ExerciseViewHolder class
    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseImage: ImageView = itemView.findViewById(R.id.iv_exercise)
        val exerciseName: TextView = itemView.findViewById(R.id.tv_exercise_name)
        val exerciseTime: TextView = itemView.findViewById(R.id.tv_exercise_time)
    }
}