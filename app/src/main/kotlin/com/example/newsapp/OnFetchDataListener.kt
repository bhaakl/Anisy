package com.example.newsapp

import com.example.newsapp.models.NewsHeadlines

interface OnFetchDataListener<NewsApiResponse> {
    fun onFetchData(list: List<NewsHeadlines>, message: String?)
    fun onError(message: String?)
}
