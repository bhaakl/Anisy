package com.bhaakl.anisy.presentation.model.anime.aired

import android.os.Parcelable
import com.bhaakl.anisy.presentation.model.anime.date_prop.Prop
import kotlinx.parcelize.Parcelize

@Parcelize
data class Aired(
    val from: String?,
    val to: String?,
    val prop: Prop?,
    val string: String?
): Parcelable