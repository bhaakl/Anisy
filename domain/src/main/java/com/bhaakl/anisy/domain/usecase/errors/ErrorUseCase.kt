package com.bhaakl.anisy.domain.usecase.errors

import com.bhaakl.anisy.domain.util.Error

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
    fun getError(exception: Exception): Error
}