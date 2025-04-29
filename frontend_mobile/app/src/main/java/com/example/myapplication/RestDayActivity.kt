package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class RestDayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rest_day)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rest_day)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageView>(R.id.btnBack)
        backButton.setOnClickListener {
            finish() // Closes the current activity and returns to the previous one
        }

        val gifImageView: ImageView = findViewById(R.id.imgRest)
        Glide.with(this)
            .asGif()
            .load(R.drawable.ic_rest) // Or use .load(R.raw.fun_dance) if in raw folder
            .into(gifImageView)

        val finishedButton = findViewById<Button>(R.id.btn_finished)
        finishedButton.setOnClickListener {
            // Create an intent to navigate to HomePageActivity
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish() // Close this activity to prevent it from staying in the back stack
        }
    }
}