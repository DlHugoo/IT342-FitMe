package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.content.Intent
import android.os.CountDownTimer
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.model.WorkoutDayExercise

class ExerciseActivity : AppCompatActivity() {

    private lateinit var exercises: List<WorkoutDayExercise>
    private var currentIndex = 0

    private lateinit var tvExerciseName: TextView
    private lateinit var tvTimerOrReps: TextView
    private lateinit var imgExercise: ImageView
    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var btnPausePlay: ImageView

    private var countDownTimer: CountDownTimer? = null
    private var remainingTimeInMillis: Long = 0L
    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.exercise)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        tvExerciseName = findViewById(R.id.tvExerciseName)
        tvTimerOrReps = findViewById(R.id.tvTimer)
        imgExercise = findViewById(R.id.imgExercise)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        btnPausePlay = findViewById(R.id.btnPausePlay)

        // Retrieve the list of exercises from the Intent
        exercises = intent.getParcelableArrayListExtra("exercises") ?: emptyList()

        if (exercises.isEmpty()) {
            finish() // Exit if no exercises are provided
            return
        }

        val backButton = findViewById<ImageView>(R.id.btnBack)
        backButton.setOnClickListener {
            finish() // Closes the current activity and returns to the previous one
        }

        // Display the first exercise
        displayExercise(currentIndex)

        btnNext.setOnClickListener {
            // Cancel the current timer and reset its state
            countDownTimer?.cancel()
            resetTimerState()

            if (currentIndex < exercises.size - 1) {
                currentIndex++
                displayExercise(currentIndex) // Display the next exercise
            } else {
                // Redirect to Exercise Complete page
                val intent = Intent(this, ExerciseCompleteActivity::class.java)
                intent.putParcelableArrayListExtra("exercises", ArrayList(exercises))
                startActivity(intent)
            }
        }

        btnPrevious.setOnClickListener {
            // Cancel the current timer and reset its state
            countDownTimer?.cancel()
            resetTimerState()

            if (currentIndex > 0) {
                currentIndex--
                displayExercise(currentIndex) // Display the previous exercise
            }
        }

        btnPausePlay.setOnClickListener {
            if (isPaused) {
                // Resume the timer for the current exercise
                startCountdownTimer((remainingTimeInMillis / 1000).toInt())
                btnPausePlay.setImageResource(R.drawable.ic_pause) // Change to pause icon
                isPaused = false
            } else {
                // Pause the timer for the current exercise
                countDownTimer?.cancel()
                btnPausePlay.setImageResource(R.drawable.ic_play) // Change to play icon
                isPaused = true
            }
        }
    }

    private fun displayExercise(index: Int) {
        val exercise = exercises[index]

        // Set exercise name
        tvExerciseName.text = exercise.exerciseName ?: "Unknown Exercise"

        // Prepend BASE_URL to gifUrl if it's a relative path
        val BASE_URL = "https://metal-cable-458215-n5.as.r.appspot.com"
        val fullGifUrl = if (exercise.gifUrl?.startsWith("/") == true) {
            BASE_URL + exercise.gifUrl
        } else {
            exercise.gifUrl
        }

        // Set GIF image using Glide
        Glide.with(this)
            .asGif()
            .load(fullGifUrl)
            .error(R.drawable.exercise_sample)
            .into(imgExercise)

        // Cancel any existing timer and reset its state
        countDownTimer?.cancel()
        resetTimerState()

        // Display either duration or reps
        if (exercise.duration != null && exercise.duration > 0) {
            // Enable pause/play button
            btnPausePlay.isEnabled = true

            // Show countdown timer with a 1-second delay
            tvTimerOrReps.text = "0:${String.format("%02d", exercise.duration)}"
            startCountdownTimerWithDelay(exercise.duration)
        } else if (exercise.reps != null && exercise.reps > 0) {
            // Disable pause/play button
            btnPausePlay.isEnabled = false

            // Show reps in "xReps" format
            tvTimerOrReps.text = "x${exercise.reps}"
        } else {
            // Disable pause/play button
            btnPausePlay.isEnabled = false

            tvTimerOrReps.text = "No data"
        }
    }

    private val handler = android.os.Handler()

    private fun startCountdownTimerWithDelay(seconds: Int) {
        // Remove any pending delayed tasks to avoid multiple timers
        handler.removeCallbacksAndMessages(null)

        // Delay the timer start by 1 second
        handler.postDelayed({
            if (!isPaused) { // Ensure the timer is not paused before starting
                startCountdownTimer(seconds)
            }
        }, 1000L)
    }

    private fun startCountdownTimer(seconds: Int) {
        remainingTimeInMillis = seconds * 1000L
        countDownTimer = object : CountDownTimer(remainingTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                val secondsRemaining = millisUntilFinished / 1000
                tvTimerOrReps.text = "0:${String.format("%02d", secondsRemaining)}"
            }

            override fun onFinish() {
                tvTimerOrReps.text = "Done!"
            }
        }.start()
    }

    private fun resetTimerState() {
        countDownTimer?.cancel() // Cancel any existing timer
        handler.removeCallbacksAndMessages(null) // Clear any pending delayed tasks
        isPaused = false // Reset pause state
        btnPausePlay.setImageResource(R.drawable.ic_pause) // Reset to pause icon
        remainingTimeInMillis = 0L // Reset remaining time
    }
}