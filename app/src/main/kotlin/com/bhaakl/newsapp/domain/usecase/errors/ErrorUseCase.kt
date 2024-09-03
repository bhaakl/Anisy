package com.bhaakl.newsapp.domain.usecase.errors

import com.bhaakl.newsapp.data.util.error.Error

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
    fun getError(exception: Exception): Error
}