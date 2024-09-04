package com.bhaakl.anisy.data.util

sealed class NetworkError {
    class Api(val error: MutableMap<String, List<String>>) : NetworkError()
    class Unexpected(val error: String) : NetworkError()
}