package com.bhaakl.anisy.presentation.model.anime.pagination

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaginationItems(
    val count: Int,
    val total: Int,
    val perPage: Int
): Parcelable