package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.signupNow.setOnClickListener {
            val intent = Intent(applicationContext, signup::class.java)
            startActivity(intent)
            finish()
        }

        /*binding.forgot.setOnClickListener {
            val intent = Intent(applicationContext, ForgotPass::class.java)
            startActivity(intent)
            finish()
        }*/


        binding.signupBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.email1.text.toString()
            val password = binding.password1.text.toString()

            if (TextUtils.isEmpty(email)){
                binding.progressBar.visibility = View.GONE
                binding.email1.error = "Email не может быть пустым"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                binding.progressBar.visibility = View.GONE
                binding.password1.error = "Пароль не может быть пустым"
                return@setOnClickListener
            }
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    binding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        //important block

//                                    if(mAuth.getCurrentUser().isEmailVerified()){
//
//                                    }
//                                    else{
//                                        Toast.makeText(login.this, "Просим првоеерить свою эл. почту на корректность!", Toast.LENGTH_SHORT).show();
//                                    }

                        Toast.makeText(
                            this@login, "Логин успешный.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@login, "Аутентификация не прошла.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}