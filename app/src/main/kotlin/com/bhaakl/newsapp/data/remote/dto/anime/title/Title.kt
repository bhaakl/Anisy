package com.bhaakl.newsapp.data.remote.dto.anime.title

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Title(
    @SerialName("type") val type: String,
    @SerialName("title") val title: String
): Parcelable