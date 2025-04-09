package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.api.WeightUpdateRequest
import com.example.myapplication.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatsPageActivity : AppCompatActivity() {

    private lateinit var currentWeightField: EditText
    private lateinit var editButton: Button
    private var isEditing: Boolean = false // Track whether we are editing or saving

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stats_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statspage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bottom Nav
        val navhome: LinearLayout = findViewById(R.id.nav_home)
        navhome.setOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))
        }

        val navprofile: LinearLayout = findViewById(R.id.nav_profile)
        navprofile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Weight Edit Logic
        currentWeightField = findViewById(R.id.tv_current_value)
        editButton = findViewById(R.id.btn_edit_weight)

        editButton.setOnClickListener {
            if (!isEditing) {
                currentWeightField.isEnabled = true
                currentWeightField.requestFocus()
                editButton.text = "Save"
                isEditing = true
            } else {
                val newWeight = currentWeightField.text.toString().toDoubleOrNull()
                if (newWeight != null) {
                    updateWeightOnServer(newWeight)
                } else {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateWeightOnServer(weight: Double) {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "No token found. Please log in again.", Toast.LENGTH_SHORT).show()
            Log.e("StatsPage", "JWT token is missing or empty.")
            return
        }

        val bearerToken = "Bearer $token"
        Log.d("StatsPage", "Token used: $bearerToken") // Debug log

        val request = WeightUpdateRequest(weight)

        RetrofitClient.instance.updateWeight(bearerToken, request)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@StatsPageActivity, "Weight updated!", Toast.LENGTH_SHORT).show()
                        currentWeightField.isEnabled = false
                        editButton.text = "Edit"
                        isEditing = false
                    } else {
                        Toast.makeText(this@StatsPageActivity, "Failed to update weight", Toast.LENGTH_SHORT).show()
                        Log.e("StatsPage", "Response code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@StatsPageActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("StatsPage", "Network error", t)
                }
            })
    }
}
