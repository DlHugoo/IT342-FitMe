package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import android.widget.Toast
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.api.WeightUpdateRequest
import com.example.myapplication.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseCompleteActivity : AppCompatActivity() {

    private lateinit var weightValueTextView: EditText
    private lateinit var updateWeightButton: Button
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise_complete)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.exercise_complete)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener {
            finish() // Closes the current activity and returns to the previous one
        }

        val homeButton = findViewById<Button>(R.id.btn_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity
        }

        val gifImageView: ImageView = findViewById(R.id.gif_trophy)
        Glide.with(this)
            .asGif()
            .load(R.drawable.completion_trophy) // Or use .load(R.raw.fun_dance) if in raw folder
            .into(gifImageView)

        // Initialize views
        weightValueTextView = findViewById(R.id.tv_weight_value)
        updateWeightButton = findViewById(R.id.btn_update_weight)

        // Fetch and display the current weight
        fetchCurrentUserWeight()

        // Handle weight update logic
        updateWeightButton.setOnClickListener {
            if (!isEditing) {
                // Enable editing
                weightValueTextView.isEnabled = true
                weightValueTextView.requestFocus()
                updateWeightButton.text = "Save"
                isEditing = true
            } else {
                // Save the updated weight
                val newWeight = weightValueTextView.text.toString().toDoubleOrNull()
                if (newWeight != null) {
                    updateWeightOnServer(newWeight)
                } else {
                    Toast.makeText(this, "Please enter a valid weight", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchCurrentUserWeight() {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val userId = sharedPref.getLong("userId", -1L)

        if (token.isNullOrEmpty() || userId == -1L) {
            Toast.makeText(this, "Missing token or user ID. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val bearerToken = "Bearer $token"

        RetrofitClient.instance.getUserById(userId, bearerToken)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        user?.let {
                            weightValueTextView.setText(it.weight?.toString() ?: "-")
                            weightValueTextView.isEnabled = false // Disable editing initially
                        }
                    } else {
                        Toast.makeText(this@ExerciseCompleteActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@ExerciseCompleteActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updateWeightOnServer(weight: Double) {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "No token found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val bearerToken = "Bearer $token"
        val request = WeightUpdateRequest(weight)

        RetrofitClient.instance.updateWeight(bearerToken, request)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ExerciseCompleteActivity, "Weight updated successfully!", Toast.LENGTH_SHORT).show()
                        weightValueTextView.isEnabled = false
                        updateWeightButton.text = "Update"
                        isEditing = false
                    } else {
                        Toast.makeText(this@ExerciseCompleteActivity, "Failed to update weight", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@ExerciseCompleteActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}