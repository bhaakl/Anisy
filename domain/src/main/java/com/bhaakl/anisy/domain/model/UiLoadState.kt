package com.bhaakl.anisy.domain.model

// A generic class that contains data and status about loading this data.
sealed class UiLoadState<T>(
    val data: T? = null,
    private val _errorCode: Int? = null,
    private val _description: String? = null
) {
    val errorCode: Int? get() = _errorCode
    val description: String? get() = _description

    class Success<T>(data: T) : UiLoadState<T>(data)
    class Loading<T>(data: T? = null) : UiLoadState<T>(data)
    class DataError<T>(errorCode: Int? = null, description: String? = null) : UiLoadState<T>(null, errorCode, description)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[errorCode=$errorCode, description=$description]"
            is Loading<T> -> "Loading"
        }
    }
}