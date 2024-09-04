package com.bhaakl.anisy.domain.util

import kotlinx.serialization.Serializable

@Serializable
class Error(val code: Int, val description: String) {
    constructor(exception: Exception) : this(
        code = DEFAULT_ERROR,
        description = if (exception.localizedMessage.isNullOrBlank()) exception.message
            ?: "" else exception.localizedMessage ?: ""
    )
}

const val NO_INTERNET_CONNECTION = -1
const val NETWORK_ERROR = -2
const val DEFAULT_ERROR = -3
const val UNEXPECTED_ERROR = -4
const val PASS_WORD_ERROR = -101
const val USER_NAME_ERROR = -102
const val CHECK_YOUR_FIELDS = -103
const val SEARCH_ERROR = -104