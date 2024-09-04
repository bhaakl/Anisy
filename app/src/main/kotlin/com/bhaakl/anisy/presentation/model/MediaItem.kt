package com.bhaakl.anisy.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(val url: String, val type: MediaType) : Parcelable
@Parcelize
enum class MediaType : Parcelable { IMAGE, VIDEO }