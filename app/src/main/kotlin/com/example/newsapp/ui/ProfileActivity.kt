package com.example.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.App
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityProfileBinding
import com.example.newsapp.room.repo.UserRepository
import com.example.newsapp.viewmodel.MainViewModel
import com.example.newsapp.viewmodel.MainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: MainViewModel by viewModels {
        val userRepository = UserRepository((applicationContext as App).database.userDao())
        MainViewModelFactory(userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(applicationContext, login::class.java))
            finish()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.selectedItemId = R.id.navigation_profile

        bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                    true
                }

                R.id.navigation_eventpick -> {
                    startActivity(Intent(this@ProfileActivity, PickEventActivity::class.java))
                    true
                }

                R.id.navigation_profile -> {
                    true
                }

                else -> false
            }
        }

        //Profile
        val profileSlug = binding.profileSlug
        val profileFio = binding.profileFio
        val profileInfoClassname = binding.profileInfoClassname
        val profileInfoBirthday = binding.profileInfoBirthday
        lifecycleScope.launch {
            viewModel.getUserData(auth.currentUser?.email.toString()).collect { userData ->
                if (userData != null) {
                    Log.e(
                        "MainViewModel.getUserData",
                        "user -> ${userData["fio"]}; ${userData["role"]}"
                    )
                    profileInfoClassname.visibility = if (userData["role"].equals("TEACHER")) {
                        View.GONE
                    } else {
                        profileInfoClassname.text = userData["className"]
                        View.VISIBLE
                    }
                    profileInfoBirthday.text = userData["birthday"]
                    profileSlug.text = userData["slugProfile"]
                    profileFio.text = userData["fio"]
                } else {
                    viewModel.getUserByEmail(auth.currentUser?.email.toString()).collect { user ->
                        if (user != null) {
                            Log.e(
                                "MainViewModel.getUserByEmail",
                                "user -> ${user.name}; ${user.role}"
                            )
                        }
                        Log.e(
                            "MainViewModel.getUserData",
                            "User with email ${auth.currentUser?.email.toString()} not found!!"
                        )
                    }
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

        val contactInfoBtn = binding.contactInfoBtn
        contactInfoBtn.setOnClickListener {
            startActivity(Intent(this, AboutUs::class.java))
        }

        //in setting
        val signoutBtn = binding.signoutBtn
        signoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, login::class.java))
            finish()
        }

    }
}