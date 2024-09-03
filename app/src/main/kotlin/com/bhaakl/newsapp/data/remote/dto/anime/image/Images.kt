package com.bhaakl.newsapp.data.remote.dto.anime.image

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Images(
    @SerialName("jpg") val jpg: ImageDetails
): Parcelable