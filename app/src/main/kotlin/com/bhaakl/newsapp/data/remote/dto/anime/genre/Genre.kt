package com.bhaakl.newsapp.data.remote.dto.anime.genre

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Genre(
    @SerialName("mal_id") val malId: Int,
    @SerialName("type") val type: String,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
): Parcelable