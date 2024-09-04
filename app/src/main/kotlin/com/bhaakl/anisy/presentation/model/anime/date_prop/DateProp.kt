package com.bhaakl.anisy.presentation.model.anime.date_prop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateProp(
    val day: Int?,
    val month: Int?,
    val year: Int?
): Parcelable