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
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.LoginRequest
import com.example.myapplication.api.LoginResponse
import com.example.myapplication.api.RetrofitClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var apiService: ApiService

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize API service
        apiService = RetrofitClient.instance // ✅ Correct

        // Handle login when the button is clicked
        loginButton.setOnClickListener {
            loginUser()
        }

        val signupLink: TextView = findViewById(R.id.signupLink)

        // Set click listener to navigate to Home Page
        signupLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
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

    private fun loginUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(email, password)
        val call = apiService.loginUser(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Save token, userId, and role in SharedPreferences
                        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("token", loginResponse.token)
                            putLong("userId", loginResponse.userId)
                            putString("email", loginResponse.email)
                            putString("role", loginResponse.role)
                            apply()
                        }

                        Toast.makeText(this@LoginActivity, "Welcome, ${loginResponse.email}!", Toast.LENGTH_SHORT).show()

                        // Navigate to MainActivity or Dashboard after successful login
                        val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error: Empty response from server!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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

            // Log successful sign-in and token for debugging
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
                            // We don't have userId in the response,
                            // but we can set email from Google account
                            putString("email", email)
                            // Default role is "user" as set in the backend
                            putString("role", "user")
                            putBoolean("google_login", true)
                            apply()
                        }

                        Toast.makeText(this@LoginActivity, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to HomePage
                        val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid response from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Google Sign-In failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("Google Sign-In", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("Google Sign-In", "Network error", t)
            }
        })
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}