package com.bhaakl.anisy.domain.model.anime.title

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Title(
    @SerialName("type") val type: String,
    @SerialName("title") val title: String
)