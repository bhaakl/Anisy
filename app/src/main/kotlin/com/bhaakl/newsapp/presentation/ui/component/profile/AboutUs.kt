package com.bhaakl.newsapp.presentation.ui.component.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.ActivityAboutUsBinding
import com.bhaakl.newsapp.ui.component.schedule.PickEventActivity
import com.bhaakl.newsapp.ui.component.newsland.NewsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AboutUs : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.navigation_profile
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, NewsActivity::class.java))
                    true
                }

                R.id.navigation_eventpick -> {
                    startActivity(Intent(this, PickEventActivity::class.java))
                    true
                }

                R.id.navigation_profile -> {
                    true
                }

                else -> false
            }
        }

        //contact info
    }
}