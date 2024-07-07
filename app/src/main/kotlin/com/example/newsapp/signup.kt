package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivitySignupBinding
import com.example.newsapp.models.Role
import com.example.newsapp.models.User
import com.example.newsapp.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private val viewModel: MainViewModel by viewModels()

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
            val className = if (binding.className.text.toString()
                    .isNotBlank()
            ) binding.className.text.toString() else null
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
                        user?.sendEmailVerification()?.addOnSuccessListener {
                            //Toast.makeText(signup.this, "Verification email has been sent..", Toast.LENGTH_SHORT).show();
                        }?.addOnFailureListener { }
                        viewModel.registerUser(
                            name = fio,
                            className = className,
                            email = email,
                            password = password,
                            role = if (className == null) Role.STUDENT else Role.TEACHER
                        )
                        Log.e("signupFbAuthUser", user?.toString() + ";")
                        Toast.makeText(this, "Аккаунт создан", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, login::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Не удалось создать аккаунт.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}