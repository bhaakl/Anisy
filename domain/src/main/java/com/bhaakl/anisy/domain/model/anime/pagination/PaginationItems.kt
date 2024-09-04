package com.bhaakl.anisy.domain.model.anime.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationItems(
    @SerialName("count") val count: Int,
    @SerialName("total") val total: Int,
    @SerialName("per_page") val perPage: Int
)