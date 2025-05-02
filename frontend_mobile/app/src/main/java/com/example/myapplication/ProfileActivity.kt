package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.model.User
import com.example.myapplication.util.CompletedWorkoutsManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var heightInput: EditText
    private lateinit var btnEdit: Button
    private lateinit var btnLogout: Button
    private lateinit var btnResetProgress: Button


    private var isEditMode = false
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        usernameInput = findViewById(R.id.usernameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        ageInput = findViewById(R.id.ageInput)
        heightInput = findViewById(R.id.heightInput)
        btnEdit = findViewById(R.id.btn_edit)
        btnLogout = findViewById(R.id.btn_logout)
        btnResetProgress = findViewById(R.id.btn_reset_progress)

        val btnBack: ImageView = findViewById(R.id.btn_back)

        // Set click listener to navigate to Home Page
        btnBack.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        val navhome: LinearLayout = findViewById(R.id.nav_home)

        // Set click listener to navigate to Home Page
        navhome.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        val navstats: LinearLayout = findViewById(R.id.nav_stats)
        navstats.setOnClickListener {
            val intent = Intent(this, StatsPageActivity::class.java)
            startActivity(intent)
        }

        // Load user profile data
        loadUserProfile()

        // Set up edit button click listener
        btnEdit.setOnClickListener {
            if (isEditMode) {
                // Save the updated profile
                saveUserProfile()
                setEditableFields(false)
                btnEdit.text = "Edit"
                isEditMode = false
            } else {
                // Enable edit mode
                setEditableFields(true)
                btnEdit.text = "Save"
                isEditMode = true
            }
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }

        // Set up reset progress button click listener
        btnResetProgress.setOnClickListener {
            resetWorkoutProgress()
        }
    }

    private fun loadUserProfile() {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPref.getLong("userId", -1L)
        val token = sharedPref.getString("token", "") ?: ""

        if (userId == -1L || token.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val authHeader = "Bearer $token" // Add "Bearer " prefix to the token
        val call = RetrofitClient.instance.getUserById(userId, authHeader)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    currentUser = response.body()
                    usernameInput.setText(currentUser?.username ?: "")
                    emailInput.setText(currentUser?.email ?: "")
                    passwordInput.setText("********")
                    ageInput.setText(currentUser?.age?.toString() ?: "")
                    heightInput.setText(currentUser?.height?.toString() ?: "")
                    setEditableFields(false)
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to load profile: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun saveUserProfile() {
        // Get current user data
        if (currentUser == null) {
            Toast.makeText(this, "User data not loaded", Toast.LENGTH_SHORT).show()
            return
        }

        // Create updated user object
        val updatedUser = currentUser!!.copy(
            username = usernameInput.text.toString(),
            // Keep the original email since it's not editable
            password = if (passwordInput.text.toString() == "********")
                currentUser!!.password
            else
                passwordInput.text.toString(),
            age = ageInput.text.toString().toIntOrNull() ?: currentUser!!.age,
            height = heightInput.text.toString().toFloatOrNull() ?: currentUser!!.height
        )

        // Get token from SharedPreferences
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Token not found, please login again", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val authHeader = "Bearer $token"  // Add "Bearer " prefix to the token

        // Make API call to update user profile
        val call = RetrofitClient.instance.updateOwnProfile(authHeader, updatedUser)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    currentUser = response.body()
                    Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ProfileActivity", "Update failed: ${response.code()}")
                    Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("ProfileActivity", "Update error: ${t.message}")
                Toast.makeText(this@ProfileActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setEditableFields(isEditable: Boolean) {
        // Make fields editable or non-editable
        usernameInput.isEnabled = isEditable
        // emailInput always stays non-editable
        emailInput.isEnabled = false
        passwordInput.isEnabled = isEditable
        ageInput.isEnabled = isEditable
        heightInput.isEnabled = isEditable
    }

    private fun logoutUser() {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // Store whether this is a Google user before clearing preferences
        val isGoogleUser = sharedPref.getBoolean("google_login", false) ||
                (sharedPref.getString("email", "")?.endsWith("@gmail.com") == true)

        // Clear all user data from SharedPreferences
        with(sharedPref.edit()) {
            clear()  // Clear all preferences instead of removing individual keys
            apply()
        }

        // If user was logged in with Google, sign out from Google as well
        if (isGoogleUser) {
            try {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("450145433567-vdg3q71jbo8im80klnpb5eftmgh6h5j0.apps.googleusercontent.com")
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(this, gso)
                googleSignInClient.signOut().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("ProfileActivity", "Google Sign-Out successful")
                    } else {
                        Log.e("ProfileActivity", "Google Sign-Out failed", task.exception)
                    }
                    // Navigate to login regardless of sign-out success
                    navigateToLogin()
                }
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Error during Google Sign-Out", e)
                // If Google Sign-Out fails, still navigate to login
                navigateToLogin()
            }
        } else {
            // For regular users, just navigate to login
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        // Navigate to LoginActivity and clear the back stack
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun resetWorkoutProgress() {
        // Create an instance of CompletedWorkoutsManager
        val completedWorkoutsManager = CompletedWorkoutsManager(this)
        
        // Clear all completed workouts
        completedWorkoutsManager.clearAllCompletedWorkouts()
        
        // Show a success message
        Toast.makeText(this, "Workout progress has been reset", Toast.LENGTH_SHORT).show()
    }
}