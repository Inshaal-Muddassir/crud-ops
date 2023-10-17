package com.example.crudsops.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudsops.Utils.isValidEmail
import com.example.crudsops.Utils.displayToast
import com.example.crudsops.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setting custom action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        auth = Firebase.auth

        // setting up on click listeners
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnSignup.setOnClickListener {
            val userEmail: String = binding.etEmail.text.trim().toString()
            val userPassword: String = binding.etPassword.text.trim().toString()

            signUp(userEmail, userPassword)
        }
    }

    private fun signUp(email: String, password: String) {
        if (!isValidEmail(email)) {
            displayToast(baseContext, "Please enter valid email address", Toast.LENGTH_SHORT)
            return
        } else if (password.isEmpty()) {
            displayToast(baseContext, "Password Required", Toast.LENGTH_SHORT)
            return
        } else if (password.length < 6) {
            displayToast(baseContext, "Minimum password length is 6", Toast.LENGTH_SHORT)
            return
        }

        // disabling buttons to prevent multi-clicks
        appInLoadingSate(true)
        // firebase auth signUp
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    displayToast(baseContext, "User Created Successfully", Toast.LENGTH_SHORT)
                    // signing out the user, to redirect to login screen
                    auth.signOut()
                    finish()
                } else {
                    displayToast(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                }
                // enabling back the buttons
                appInLoadingSate(false)
            }
    }

    private fun appInLoadingSate(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSignup.isEnabled = false
            binding.btnSignup.isClickable = false
            binding.ivBack.isEnabled = false
            binding.ivBack.isClickable = false
        } else {
            binding.btnSignup.isEnabled = true
            binding.btnSignup.isClickable = true
            binding.ivBack.isEnabled = true
            binding.ivBack.isClickable = true
        }
    }
}