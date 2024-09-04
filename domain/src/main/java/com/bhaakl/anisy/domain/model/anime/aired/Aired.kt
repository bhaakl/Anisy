package com.bhaakl.anisy.domain.model.anime.aired

import com.bhaakl.anisy.domain.model.anime.date_prop.Prop
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Aired(
    @SerialName("from") val from: String?,
    @SerialName("to") val to: String?,
    @SerialName("prop") val prop: Prop?,
    @SerialName("string") val string: String?
)