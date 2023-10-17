package com.example.crudsops.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudsops.Utils.displayToast
import com.example.crudsops.Utils.isValidEmail
import com.example.crudsops.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setting custom action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        auth = Firebase.auth

        // setting up on click listeners
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnSendResetLink.setOnClickListener {
            val userEmail = binding.etEmail.text.trim().toString()
            resetPassword(userEmail)
        }
    }

    private fun resetPassword(email: String) {
        if (!isValidEmail(email)) {
            displayToast(baseContext, "Invalid Email", Toast.LENGTH_SHORT)
            return
        }

        // disabling buttons to prevent multi-clicks
        appInLoadingSate(true)
        // firebase auth send reset link
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    displayToast(baseContext, "Reset Email Sent.", Toast.LENGTH_SHORT)
                    finish()
                } else {
                    displayToast(baseContext, "Unable to send email.", Toast.LENGTH_SHORT)
                }
                // enabling back the buttons
                appInLoadingSate(false)
            }
    }

    private fun appInLoadingSate(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSendResetLink.isEnabled = false
            binding.btnSendResetLink.isClickable = false
            binding.ivBack.isEnabled = false
            binding.ivBack.isClickable = false
        } else {
            binding.btnSendResetLink.isEnabled = true
            binding.btnSendResetLink.isClickable = true
            binding.ivBack.isEnabled = true
            binding.ivBack.isClickable = true
        }
    }
}