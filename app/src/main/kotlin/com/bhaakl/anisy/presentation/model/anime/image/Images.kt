package com.bhaakl.anisy.presentation.model.anime.image

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Images(
    val jpg: ImageDetails
): Parcelable