package com.bhaakl.anisy.presentation.ui.fragments.login.signUp

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.anisy.R
import com.bhaakl.anisy.databinding.FragmentSignUpBinding
import com.bhaakl.anisy.data.datasource.local.entity.Role
import com.bhaakl.anisy.presentation.extensions.navigateSafely
import com.bhaakl.anisy.presentation.ui.fragments.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpFragment: Fragment(R.layout.fragment_sign_up) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private lateinit var mAuth: FirebaseAuth
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //register
        mAuth = FirebaseAuth.getInstance()

        binding.loginNow.setOnClickListener {
            findNavController().navigateSafely(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.signupBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val fio = binding.fio.text.toString()
            val className = binding.className.text.toString().ifBlank { null }
            val email = binding.email1.text.toString()
            val password =
                if (binding.password1.text.toString() == binding.confirmPassword.text.toString()) {
                    binding.password1.text.toString()

                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Пароли должны совпадать!", Toast.LENGTH_SHORT).show()
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
                        viewModel.registerUser(
                            name = fio,
                            className = className,
                            email = email,
                            password = password,
                            role = if (className == null) Role.TEACHER else Role.STUDENT
                        )
                        Log.d("signup", "User created: ${user?.toString()}")
                        findNavController().navigateSafely(R.id.action_signUpFragment_to_signInFragment)
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthUserCollisionException) {
                            // Email уже используется
                            Toast.makeText(
                                requireContext(),
                                "Этот email уже используется. Пожалуйста, войдите с этим email.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.e("signup", "Failed to create user", exception)
                            val errorMessage = exception?.message ?: "Не удалось создать аккаунт"
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }
}