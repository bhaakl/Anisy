package com.bhaakl.anisy.domain.usecase.fetchanime

import androidx.paging.PagingData
import com.bhaakl.anisy.domain.repository.AnimeRepository
import com.bhaakl.anisy.domain.model.anime.Anime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FetchScheduleAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository,
    private val ioDispatcher: CoroutineContext
) {
    operator fun invoke() : Flow<PagingData<Anime>> = flow {
        emitAll(repository.fetchScheduleAnime())
    }.flowOn(ioDispatcher)
}