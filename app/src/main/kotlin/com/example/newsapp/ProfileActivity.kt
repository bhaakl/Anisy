package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.newsapp.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity(){
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

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