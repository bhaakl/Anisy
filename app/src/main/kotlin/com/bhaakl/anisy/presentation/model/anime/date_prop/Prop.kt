package com.bhaakl.anisy.presentation.model.anime.date_prop

import android.os.Parcelable
import com.bhaakl.anisy.presentation.model.anime.date_prop.DateProp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prop(
    val from: DateProp?,
    val to: DateProp?
): Parcelable

