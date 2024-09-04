package com.bhaakl.anisy.data.datasource.remote.dto.anime

import com.bhaakl.anisy.domain.model.anime.pagination.Pagination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimePagingResponse<T> (
    @SerialName("data") val data: MutableList<T>,
    @SerialName("pagination") val pagination: Pagination,
)