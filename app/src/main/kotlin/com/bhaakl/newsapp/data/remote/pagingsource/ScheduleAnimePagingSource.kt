package com.bhaakl.newsapp.data.remote.pagingsource

import android.content.Context
import com.bhaakl.newsapp.data.base.BasePagingSource
import com.bhaakl.newsapp.data.remote.dto.anime.AnimeDto
import com.bhaakl.newsapp.data.remote.apiservice.jikan.JikanApiService
import com.bhaakl.newsapp.domain.model.anime.Anime

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