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
import com.example.myapplication.api.WeightLogEntity
import com.example.myapplication.api.WeightUpdateRequest
import com.example.myapplication.component.CustomLineChart
import com.example.myapplication.model.User
import com.example.myapplication.model.WeightLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatsPageActivity : AppCompatActivity() {

    private lateinit var currentWeightField: EditText
    private lateinit var editButton: Button
    private var isEditing: Boolean = false // Track whether we are editing or saving
    private lateinit var weightLineChart: CustomLineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stats_page)
        weightLineChart = findViewById(R.id.weight_line_chart)

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

        fetchCurrentUserData()

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

        fetchCurrentUserData()

        val token = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("token", null)
        token?.let { fetchAndDisplayWeightLogs("Bearer $it") }

        fetchUserDataAndCalculateBMI()

    }

    private fun fetchCurrentUserData() {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val userId = sharedPref.getLong("userId", -1L) // Make sure this key matches what was used during login

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
                            val weightText = it.weight?.toString() ?: "-"
                            currentWeightField.setText(weightText)
                        }
                    } else {
                        Toast.makeText(this@StatsPageActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@StatsPageActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
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


                        // ✅ Log the weight in the backend (this logs a new entry)
                        logWeight(weight, bearerToken)

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

    private fun logWeight(weight: Double, bearerToken: String) {
        RetrofitClient.instance.logWeight(bearerToken, WeightLogEntity(weight))
            .enqueue(object : Callback<WeightLog> {
                override fun onResponse(call: Call<WeightLog>, response: Response<WeightLog>) {
                    if (response.isSuccessful) {
                        fetchAndDisplayWeightLogs(bearerToken)     // ✅ Update UI
                    } else {
                        Log.e("StatsPage", "Failed to log weight")
                    }
                }

                override fun onFailure(call: Call<WeightLog>, t: Throwable) {
                    Log.e("StatsPage", "Error logging weight", t)
                }
            })
    }

    private fun fetchAndDisplayWeightLogs(bearerToken: String) {
        RetrofitClient.instance.getWeightLogs(bearerToken)
            .enqueue(object : Callback<List<WeightLog>> {
                override fun onResponse(call: Call<List<WeightLog>>, response: Response<List<WeightLog>>) {
                    if (response.isSuccessful) {
                        val logs = response.body()
                        if (!logs.isNullOrEmpty()) {
                            val heaviest = logs.maxByOrNull { it.weight }?.weight
                            val lightest = logs.minByOrNull { it.weight }?.weight

                            findViewById<TextView>(R.id.tv_heaviest_value).text = heaviest?.toString() ?: "-"
                            findViewById<TextView>(R.id.tv_lightest_value).text = lightest?.toString() ?: "-"

                            // ✅ Set chart data
                            val weightsAsFloats = logs.map { it.weight.toFloat() }.reversed()
                            weightLineChart.setData(weightsAsFloats)
                        }
                    } else {
                        Log.e("StatsPage", "Failed to fetch logs")
                    }
                }

                override fun onFailure(call: Call<List<WeightLog>>, t: Throwable) {
                    Log.e("StatsPage", "Error fetching logs", t)
                }
            })
    }

    private fun calculateBMI(weight: Double, height: Double) {
        if (height > 0) {
            // Convert height to meters if it's in centimeters (e.g., 170 cm = 1.70 meters)

            // Calculate BMI: weight (kg) / height (m)²
            val bmi = weight / (height* height)

            // Update the BMI value in the UI
            findViewById<TextView>(R.id.tv_bmi_value).text = String.format("%.2f", bmi)

            // Categorize BMI
            val bmiCategory = when {
                bmi < 18.5 -> "Underweight"
                bmi in 18.5..24.9 -> "Healthy Weight"
                bmi in 25.0..29.9 -> "Overweight"
                else -> "Obesity"
            }

            // Update the BMI category in the UI
            findViewById<TextView>(R.id.tv_bmi_category).text = bmiCategory
        } else {
            Toast.makeText(this, "Height must be greater than 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserDataAndCalculateBMI() {
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
                            val weight = (it.weight ?: 0.0).toDouble()
                            val height = (it.height ?: 0.0).toDouble()
                            // Call the BMI calculation function
                            calculateBMI(weight, height)
                        }
                    } else {
                        Toast.makeText(this@StatsPageActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@StatsPageActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}
