package com.bhaakl.newsapp.domain.usecase.errors

import com.bhaakl.newsapp.data.util.error.mapper.ErrorMapper
import com.bhaakl.newsapp.data.util.error.Error
import javax.inject.Inject

class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }

    override fun getError(exception: Exception): Error {
        return Error(exception)
    }
}