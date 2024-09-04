package com.bhaakl.anisy.domain.model.anime.date_prop

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DateProp(
    @SerialName("day") val day: Int?,
    @SerialName("month") val month: Int?,
    @SerialName("year") val year: Int?
)