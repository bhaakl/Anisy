package com.bhaakl.anisy.presentation.model.anime.trailer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trailer(
    val youtubeId: String?,
    val url: String?,
    val embedUrl: String?
): Parcelable
