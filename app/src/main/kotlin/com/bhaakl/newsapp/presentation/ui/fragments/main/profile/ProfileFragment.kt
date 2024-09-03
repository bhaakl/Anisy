package com.bhaakl.newsapp.presentation.ui.fragments.main.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.FragmentProfileBinding
import com.bhaakl.newsapp.presentation.extensions.navigateSafely
import com.bhaakl.newsapp.presentation.ui.fragments.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private lateinit var auth: FirebaseAuth
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        //Profile
        val profileSlug = binding.profileSlug
        val profileFio = binding.profileFio
        val profileInfoBirthday = binding.profileInfoBirthday
        lifecycleScope.launch {
            viewModel.getUserData(auth.currentUser?.email.toString()).collect { userData ->
                if (userData != null) {
                    Log.e(
                        "ViewModel.getUserData",
                        "user -> ${userData["fio"]}; ${userData["role"]}"
                    )
                    profileInfoBirthday.text = userData["birthday"]
                    profileSlug.text = userData["slugProfile"]
                    profileFio.text = userData["fio"]
                } else {
                    Log.e(
                        "LoginViewModel.getUserData",
                        "User with email ${auth.currentUser?.email.toString()} not found!!"
                    )
                }
            }
        }

        //profile routes
        val settingBtn = binding.settingBtn
        val profileRoutes = binding.profileRoutes
        val inSetting = binding.inSettingLayout
        settingBtn.setOnClickListener {
            profileRoutes.visibility = View.INVISIBLE
            inSetting.visibility = View.VISIBLE
        }

        /*val contactInfoBtn = binding.contactInfoBtn
        contactInfoBtn.setOnClickListener {
            startActivity(Intent(this, AboutUs::class.java))
        }*/

        //in setting
        val signoutBtn = binding.signoutBtn
        signoutBtn.setOnClickListener {
            findNavController().navigateSafely(R.id.action_ProfileFragment_to_signInFragment)
        }

    }
}