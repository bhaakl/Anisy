package com.bhaakl.anisy.domain.model.anime.image

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Images(
    @SerialName("jpg") val jpg: ImageDetails
)