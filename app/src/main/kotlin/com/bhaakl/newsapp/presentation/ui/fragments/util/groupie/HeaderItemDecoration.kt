package com.bhaakl.newsapp.presentation.ui.fragments.util.groupie

import androidx.annotation.ColorInt
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.presentation.ui.fragments.util.groupie.decoration.HeaderItemDecoration


class HeaderItemDecoration(
    @ColorInt background: Int,
    sidePaddingPixels: Int
) : HeaderItemDecoration(background, sidePaddingPixels, R.layout.grpie_header_item)