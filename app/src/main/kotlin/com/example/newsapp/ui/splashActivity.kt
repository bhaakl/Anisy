package com.example.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivitySplashBinding

class splashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var top: Animation
    private lateinit var bottom: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        window.navigationBarColor = ContextCompat.getColor(this, R.color.my_color_700)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_color_700)

        top = AnimationUtils.loadAnimation(this, R.anim.top)
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom)

        binding.spImg.animation = top
        binding.spText.animation = bottom

        val iHome = Intent(this@splashActivity, login::class.java)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(iHome)
            finish()
        }, 2000)
    }
}