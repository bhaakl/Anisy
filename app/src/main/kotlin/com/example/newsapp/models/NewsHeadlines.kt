package com.example.newsapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsHeadlines(
    val source: Source? = null,
    val author: String? = null,
    val title: String,
    val description: String? = null,
    val url: String,val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
) : Parcelable