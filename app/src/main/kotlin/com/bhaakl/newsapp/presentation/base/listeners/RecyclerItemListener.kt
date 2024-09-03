package com.bhaakl.newsapp.presentation.base.listeners

import com.bhaakl.newsapp.presentation.model.AnimeUi

interface RecyclerItemListener {
    fun onItemSelected(anime : AnimeUi)
}