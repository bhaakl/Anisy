package com.example.newsapp.utils

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityWeb1Binding

class webView : AppCompatActivity() {

    private lateinit var binding: ActivityWeb1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeb1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //webView = findViewById(R.id.web_view);
        val url = intent.getStringExtra("url")
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadsImagesAutomatically = true
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        url?.let { binding.webView.loadUrl(it) }
    }
}