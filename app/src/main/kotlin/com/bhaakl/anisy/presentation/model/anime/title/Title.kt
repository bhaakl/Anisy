package com.bhaakl.anisy.presentation.model.anime.title

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Title(
    val type: String,
    val title: String
): Parcelable