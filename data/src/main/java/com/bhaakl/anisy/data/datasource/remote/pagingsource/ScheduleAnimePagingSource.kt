package com.bhaakl.anisy.data.datasource.remote.pagingsource

import android.content.Context
import com.bhaakl.anisy.data.base.BasePagingSource
import com.bhaakl.anisy.data.datasource.remote.dto.anime.AnimeDto
import com.bhaakl.anisy.data.datasource.remote.apiservice.jikan.JikanApiService
import com.bhaakl.anisy.domain.model.anime.Anime

class ScheduleAnimePagingSource (
    context: Context,
    private val service: JikanApiService,
    private val filter: String,
    private val sfw: Boolean,
    private val unapproved: Boolean,
    private val limit: Int
) : BasePagingSource<AnimeDto, Anime>(
    context,
    { page -> service.requestScheduleAnime(filter, sfw, unapproved, page, limit) }
)