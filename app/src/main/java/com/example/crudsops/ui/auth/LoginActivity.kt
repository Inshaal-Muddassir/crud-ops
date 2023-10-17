package com.example.crudsops.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudsops.Utils
import com.example.crudsops.Utils.displayToast
import com.example.crudsops.databinding.ActivityLoginBinding
import com.example.crudsops.ui.mainApp.dashboard.DashBoardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // checking whether the user is already logged in
        // if yes redirecting to dash board activity
        isUserAlreadyLoggedIn()

        // setting up on clicks
        binding.btnLogin.setOnClickListener {
            val userEmail: String = binding.etEmail.text.trim().toString()
            val userPassword: String = binding.etPassword.text.trim().toString()
            signIn(userEmail, userPassword)
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(baseContext, ResetPasswordActivity::class.java))
        }
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(baseContext, SignupActivity::class.java))
        }
    }

    private fun isUserAlreadyLoggedIn() {
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(baseContext, DashBoardActivity::class.java))
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        if (!Utils.isValidEmail(email)) {
            displayToast(baseContext, "Please enter valid email address", Toast.LENGTH_SHORT)
            return
        } else if (password.isEmpty()) {
            displayToast(baseContext, "Password Required", Toast.LENGTH_SHORT)
            return
        } else if (password.length < 6) {
            displayToast(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
            return
        }

        // disabling buttons to prevent multi-clicks
        appInLoadingSate(true)
        // firebase auth signIn
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(baseContext, DashBoardActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    displayToast(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                }
                // enabling back the buttons
                appInLoadingSate(false)
            }
    }

    private fun appInLoadingSate(isLoading: Boolean) {
        if (isLoading) {
            binding.btnLogin.isEnabled = false
            binding.btnLogin.isClickable = false
            binding.btnSignup.isEnabled = false
            binding.btnSignup.isClickable = false
        } else {
            binding.btnLogin.isEnabled = true
            binding.btnLogin.isClickable = true
            binding.btnSignup.isEnabled = true
            binding.btnSignup.isClickable = true
        }
    }
}