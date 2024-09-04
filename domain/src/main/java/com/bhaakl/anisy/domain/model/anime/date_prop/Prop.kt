package com.bhaakl.anisy.domain.model.anime.date_prop

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Prop(
    @SerialName("from") val from: DateProp?,
    @SerialName("to") val to: DateProp?
)
