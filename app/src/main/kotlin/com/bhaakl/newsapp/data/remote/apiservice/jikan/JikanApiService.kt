package com.bhaakl.newsapp.data.remote.apiservice.jikan

import com.bhaakl.newsapp.data.remote.dto.anime.AnimeDto
import com.bhaakl.newsapp.data.remote.dto.anime.AnimePagingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApiService {
    @GET("top/anime")
    suspend fun requestTopAnime(
        @Query("type") type: String? = null,
        @Query("filter") filter: String? = null,
        @Query("rating") rating: String? = null,
        @Query("sfw") sfw: Boolean? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<AnimePagingResponse<AnimeDto>>

    @GET("schedules")
    suspend fun requestScheduleAnime(
        @Query("filter") filter: String? = null,
        @Query("sfw") sfw: Boolean? = null,
        @Query("unapproved") unapproved: Boolean? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<AnimePagingResponse<AnimeDto>>
}
