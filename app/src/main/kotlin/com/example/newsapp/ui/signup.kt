package com.example.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.App
import com.example.newsapp.databinding.ActivitySignupBinding
import com.example.newsapp.models.Role
import com.example.newsapp.room.repo.UserRepository
import com.example.newsapp.viewmodel.MainViewModel
import com.example.newsapp.viewmodel.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private val viewModel: MainViewModel by viewModels {
        val userRepository = UserRepository((applicationContext as App).database.userDao())
        MainViewModelFactory(userRepository)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //register
        mAuth = FirebaseAuth.getInstance()

        binding.loginNow.setOnClickListener {
            startActivity(Intent(applicationContext, login::class.java))
            finish()
        }

        binding.signupBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val fio = binding.fio.text.toString()
            val className = binding.className.text.toString().ifBlank { null }
            val birthday = binding.bday.text.toString()
            val email = binding.email1.text.toString()
            val password =
                if (binding.password1.text.toString() == binding.confirmPassword.text.toString()) {
                    binding.password1.text.toString()

                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Пароли должны совпадать!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

            if (TextUtils.isEmpty(fio)) {
                binding.progressBar.visibility = View.GONE
                binding.fio.error = "ФИО не может быть пустым!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(birthday)) {
                binding.progressBar.visibility = View.GONE
                binding.bday.error = "День рождения не может быть пустым!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                binding.progressBar.visibility = View.GONE
                binding.email1.error = "Электронная почта не может быть пустой!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                binding.progressBar.visibility = View.GONE
                binding.password1.error = "Пароль не может быть пустым!"
                return@setOnClickListener
            } else if (password.length < 6) {
                binding.progressBar.visibility = View.GONE
                binding.password1.error = "Пароль должен содержать не менее 6 символов!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(binding.confirmPassword.text.toString())) {
                binding.progressBar.visibility = View.GONE
                binding.confirmPassword.error = "Пароль не может быть пустым!"
                return@setOnClickListener
            } else if (binding.confirmPassword.text.toString().length < 6) {
                binding.progressBar.visibility = View.GONE
                binding.password1.error = "Пароль должен содержать не менее 6 символов!"
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    binding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        viewModel.registerUser(
                            name = fio,
                            className = className,
                            birthday = birthday,
                            email = email,
                            password = password,
                            role = if (className == null) Role.TEACHER else Role.STUDENT
                        )
                        Log.d("signup", "User created: ${user?.toString()}")
                        Toast.makeText(this, "Аккаунт создан.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, login::class.java))
                        finish()
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthUserCollisionException) {
                            // Email уже используется
                            Toast.makeText(
                                this,
                                "Этот email уже используется. Пожалуйста, войдите с этим email.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.e("signup", "Failed to create user", exception)
                            val errorMessage = exception?.message ?: "Не удалось создать аккаунт"
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }
}