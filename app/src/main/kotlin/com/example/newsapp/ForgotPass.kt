package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityForgotPassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPass : AppCompatActivity() {private lateinit var binding: ActivityForgotPassBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.forgotBtn.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.forgetEmail.text.toString()
        if (email.isEmpty()) {binding.forgetEmail.error = "Required"
        } else {
            forgetPass(email)
        }
    }

    private fun forgetPass(email: String) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Check your Email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, login::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Error: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}