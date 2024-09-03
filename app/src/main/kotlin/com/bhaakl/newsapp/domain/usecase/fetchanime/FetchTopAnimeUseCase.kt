package com.bhaakl.newsapp.domain.usecase.fetchanime

import androidx.paging.PagingData
import com.bhaakl.newsapp.domain.repository.AnimeRepository
import com.bhaakl.newsapp.domain.model.anime.Anime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FetchTopAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository,
    private val ioDispatcher: CoroutineContext
) {
    operator fun invoke(): Flow<PagingData<Anime>> = flow {
        emitAll(repository.fetchTopAnime())
    } .flowOn(ioDispatcher)
}