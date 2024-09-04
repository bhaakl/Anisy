package com.bhaakl.anisy.presentation.model.anime.genre

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val malId: Int,
    val type: String,
    val name: String,
    val url: String
): Parcelable