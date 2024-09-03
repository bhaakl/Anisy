package com.bhaakl.newsapp.data.remote.dto.anime.trailer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Trailer(
    @SerialName("youtube_id") val youtubeId: String?,
    @SerialName("url") val url: String?,
    @SerialName("embed_url") val embedUrl: String?
): Parcelable
