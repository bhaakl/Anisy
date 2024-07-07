package com.example.newsapp

import com.example.newsapp.models.NewsHeadlines

interface SelectListener {
    fun onNewsClicked(headlines: NewsHeadlines?)
}
