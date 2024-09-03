package com.bhaakl.newsapp.domain.repository

import androidx.paging.PagingData
import com.bhaakl.newsapp.domain.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun fetchTopAnime(): Flow<PagingData<Anime>>
    fun fetchScheduleAnime(): Flow<PagingData<Anime>>
}