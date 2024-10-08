package com.bhaakl.anisy.domain.model.anime.image

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDetails(
    @SerialName("image_url") val imageUrl: String,
    @SerialName("small_image_url") val smallImageUrl: String,
    @SerialName("large_image_url") val largeImageUrl: String
)