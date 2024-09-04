package com.bhaakl.anisy.data.repository

import android.content.Context
import androidx.paging.PagingData
import com.bhaakl.anisy.data.base.BaseRepository
import com.bhaakl.anisy.data.datasource.remote.apiservice.jikan.JikanApiService
import com.bhaakl.anisy.data.datasource.remote.pagingsource.ScheduleAnimePagingSource
import com.bhaakl.anisy.data.datasource.remote.pagingsource.TopAnimePagingSource
import com.bhaakl.anisy.domain.model.anime.Anime
import com.bhaakl.anisy.domain.repository.AnimeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AnimeRepositoryImpl @Inject constructor(
    private val service: JikanApiService,
    private val _ioDispatcher: CoroutineContext,
    @ApplicationContext private val context: Context
) : BaseRepository(), AnimeRepository {

    override val ioDispatcher: CoroutineContext
        get() = _ioDispatcher

    override fun fetchTopAnime(): Flow<PagingData<Anime>> =
        doPagingRequest(
            TopAnimePagingSource(
                context = context,
                service = service,
                type = "movie",
                filter = "bypopularity",
                rating = "r17",
                sfw = false,
                limit = 5
            )
        )

    override fun fetchScheduleAnime(): Flow<PagingData<Anime>> =
        doPagingRequest(
            ScheduleAnimePagingSource(
                context = context,
                service = service,
                filter = "unknown",
                sfw = false,
                unapproved = false,
                limit = 15
            )
        )
}