package com.bhaakl.anisy.domain.repository

import androidx.paging.PagingData
import com.bhaakl.anisy.domain.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun fetchTopAnime(): Flow<PagingData<Anime>>
    fun fetchScheduleAnime(): Flow<PagingData<Anime>>
}