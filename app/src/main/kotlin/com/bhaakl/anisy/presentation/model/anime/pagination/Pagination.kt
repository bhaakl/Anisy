package com.bhaakl.anisy.presentation.model.anime.pagination

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pagination(
    val lastVisiblePage: Int,
    val hasNextPage: Boolean,
    val items: PaginationItems
): Parcelable