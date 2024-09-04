package com.bhaakl.anisy.data.util.error.mapper

import android.content.Context
import com.bhaakl.anisy.data.R
import com.bhaakl.anisy.domain.util.CHECK_YOUR_FIELDS
import com.bhaakl.anisy.domain.util.NETWORK_ERROR
import com.bhaakl.anisy.domain.util.NO_INTERNET_CONNECTION
import com.bhaakl.anisy.domain.util.PASS_WORD_ERROR
import com.bhaakl.anisy.domain.util.SEARCH_ERROR
import com.bhaakl.anisy.domain.util.UNEXPECTED_ERROR
import com.bhaakl.anisy.domain.util.USER_NAME_ERROR
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorMapper @Inject constructor(@ApplicationContext private val context: Context) :
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