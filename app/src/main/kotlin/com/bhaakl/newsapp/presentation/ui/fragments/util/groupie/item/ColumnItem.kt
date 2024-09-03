package com.bhaakl.newsapp.presentation.ui.fragments.util.groupie.item

import androidx.annotation.ColorInt

class ColumnItem(
    @ColorInt colorInt: Int, index: Int
) : CardItem(colorInt, index.toString()) {

    /*init {
        extras[INSET_TYPE_KEY] = INSET
    }*/

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount / 2
}