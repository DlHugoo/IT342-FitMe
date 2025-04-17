package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class ExerciseCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise_complete)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.exercise_complete)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gifImageView: ImageView = findViewById(R.id.gif_trophy)
        Glide.with(this)
            .asGif()
            .load(R.drawable.completion_trophy) // Or use .load(R.raw.fun_dance) if in raw folder
            .into(gifImageView)

    }
}