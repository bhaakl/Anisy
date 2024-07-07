package com.example.newsapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsApiResponse(
    val status: String? = null,
    val totalResults: Int = 0,
    val articles: List<NewsHeadlines> = emptyList() ) : Parcelable