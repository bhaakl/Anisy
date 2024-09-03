package com.bhaakl.newsapp.data.remote.dto.anime.aired

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class DateProp(
    @SerialName("day") val day: Int?,
    @SerialName("month") val month: Int?,
    @SerialName("year") val year: Int?
): Parcelable