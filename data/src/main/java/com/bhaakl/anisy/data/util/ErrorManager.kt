package com.bhaakl.anisy.data.util

import com.bhaakl.anisy.domain.usecase.errors.ErrorUseCase
import com.bhaakl.anisy.data.util.error.mapper.ErrorMapper
import com.bhaakl.anisy.domain.util.Error
import javax.inject.Inject

class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }

    override fun getError(exception: Exception): Error {
        return Error(exception)
    }
}