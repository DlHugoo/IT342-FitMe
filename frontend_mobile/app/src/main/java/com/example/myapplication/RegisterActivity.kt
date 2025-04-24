package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var userNameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var btnSignUp: Button
    private lateinit var loginLink: TextView

    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize input fields and buttons
        emailInput = findViewById(R.id.emailInput)
        userNameInput = findViewById(R.id.userNameInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        btnSignUp = findViewById(R.id.btnSignUp)
        loginLink = findViewById(R.id.loginLink)

        // Handle Sign Up button click
        btnSignUp.setOnClickListener {
            registerUser()
        }

        val loginLink: TextView = findViewById(R.id.loginLink)

        // Set click listener to navigate to Home Page
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("450145433567-vdg3q71jbo8im80klnpb5eftmgh6h5j0.apps.googleusercontent.com") // Add your client ID here
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set click listener for Google Sign-In button
        findViewById<LinearLayout>(R.id.googleSignInButton).setOnClickListener {
            signInWithGoogle()
        }

    }

    private fun registerUser() {
        val username = userNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if passwords match
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new User object
        val newUser = User(
            username = username,
            email = email,
            password = password,
            role = "user",  // Default role to 'user' when signing up
            weight = null,
            height = null
        )

        // Call createUser API
        RetrofitClient.instance.createUser(newUser).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "User registered successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("API_ERROR", t.message.toString())
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun signInWithGoogle() {
        // Sign out first to clear any cached credentials
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Get ID token from Google account
            val idToken = account?.idToken

            Log.d("Google Sign-In", "Sign-in successful, email: ${account?.email}, name: ${account?.displayName}")
            Log.d("Google Sign-In", "Token: ${idToken?.take(20)}...")

            if (idToken != null) {
                // Call backend API to verify and process Google sign-in
                processGoogleSignIn(idToken, account.email ?: "")
            } else {
                Toast.makeText(this, "Failed to get ID token from Google", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            // More detailed error logging
            Log.e("Google Sign-In", "signInResult:failed code=${e.statusCode}, message: ${e.message}, status: ${e.status}")

            when (e.statusCode) {
                10 -> {
                    Log.e("Google Sign-In", "DEVELOPER_ERROR: Check your Google Cloud Console configuration")
                    Toast.makeText(this, "Google Sign-In configuration error. Please check logs.", Toast.LENGTH_LONG).show()
                }
                else -> Toast.makeText(this, "Google Sign-In failed. Error code: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processGoogleSignIn(idToken: String, email: String) {
        // Show loading indicator
        Toast.makeText(this, "Processing Google Sign-In...", Toast.LENGTH_SHORT).show()

        // Call the mobile-login endpoint
        RetrofitClient.instance.loginFromMobile(idToken).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.containsKey("token")) {
                        // Save token in SharedPreferences
                        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("token", loginResponse["token"])
                            // We don't have userId, email, and role in the response,
                            // but we can set email from Google account
                            putString("email", email)
                            // Default role is "user" as set in the backend
                            putString("role", "user")
                            putBoolean("google_login", true)
                            apply()
                        }

                        Toast.makeText(this@RegisterActivity, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to HomePage
                        val intent = Intent(this@RegisterActivity, HomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Invalid response from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Google Sign-In failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("Google Sign-In", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("Google Sign-In", "Network error", t)
            }
        })
    }


    companion object {
        private const val RC_SIGN_IN = 9001
    }
}