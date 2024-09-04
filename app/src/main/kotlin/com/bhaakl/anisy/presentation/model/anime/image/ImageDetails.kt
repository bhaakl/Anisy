package com.bhaakl.anisy.presentation.model.anime.image

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageDetails(
    val imageUrl: String,
    val smallImageUrl: String,
    val largeImageUrl: String
): Parcelable
