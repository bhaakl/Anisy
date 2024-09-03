package com.bhaakl.newsapp.data.remote.dto.anime.aired

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Aired(
    @SerialName("from") val from: String?,
    @SerialName("to") val to: String?,
    @SerialName("prop") val prop: Prop?,
    @SerialName("string") val string: String?
): Parcelable