package com.bhaakl.newsapp.data.remote.dto.anime.aired

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Prop(
    @SerialName("from") val from: DateProp?,
    @SerialName("to") val to: DateProp?
): Parcelable

