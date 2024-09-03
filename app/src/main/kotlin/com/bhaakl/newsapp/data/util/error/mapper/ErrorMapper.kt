package com.bhaakl.newsapp.data.util.error.mapper

import android.content.Context
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.data.util.error.CHECK_YOUR_FIELDS
import com.bhaakl.newsapp.data.util.error.NETWORK_ERROR
import com.bhaakl.newsapp.data.util.error.NO_INTERNET_CONNECTION
import com.bhaakl.newsapp.data.util.error.PASS_WORD_ERROR
import com.bhaakl.newsapp.data.util.error.SEARCH_ERROR
import com.bhaakl.newsapp.data.util.error.UNEXPECTED_ERROR
import com.bhaakl.newsapp.data.util.error.USER_NAME_ERROR
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorMapper @Inject constructor(@ApplicationContext val context: Context) :
    ErrorMapperSource {

    override fun getErrorString(errorId: Int): String {
        return context.getString(errorId)
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(
            Pair(NO_INTERNET_CONNECTION, getErrorString(R.string.no_internet)),
            Pair(NETWORK_ERROR, getErrorString(R.string.network_error)),
            Pair(UNEXPECTED_ERROR, getErrorString(R.string.unexpected_error)),
            Pair(PASS_WORD_ERROR, getErrorString(R.string.invalid_password)),
            Pair(USER_NAME_ERROR, getErrorString(R.string.invalid_username)),
            Pair(CHECK_YOUR_FIELDS, getErrorString(R.string.invalid_username_and_password)),
            Pair(SEARCH_ERROR, getErrorString(R.string.search_error))
        ).withDefault { getErrorString(R.string.network_error) }
}