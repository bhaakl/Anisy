package com.bhaakl.anisy.presentation.base.listeners

import com.bhaakl.anisy.presentation.model.anime.AnimeUi

interface RecyclerItemListener {
    fun onItemSelected(anime : AnimeUi)
}