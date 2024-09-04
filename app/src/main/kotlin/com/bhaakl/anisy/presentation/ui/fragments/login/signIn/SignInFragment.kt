package com.bhaakl.anisy.presentation.ui.fragments.login.signIn

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.anisy.R
import com.bhaakl.anisy.databinding.FragmentSignInBinding
import com.bhaakl.anisy.presentation.extensions.activityNavController
import com.bhaakl.anisy.presentation.extensions.navigateSafely
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private lateinit var mAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        binding.signupNow.setOnClickListener {
            findNavController().navigateSafely(R.id.action_signInFragment_to_signUpFragment)
        }

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
                            requireContext(), "Логин успешный.",
                            Toast.LENGTH_SHORT
                        ).show()
                        activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                    } else {
                        Toast.makeText(
                            requireContext(), "Аутентификация не прошла.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

}