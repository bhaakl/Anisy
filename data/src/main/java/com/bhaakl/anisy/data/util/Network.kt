package com.bhaakl.anisy.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.delay
import javax.inject.Inject

class Network @Inject constructor(private val context: Context) : NetworkConnectivity {
    override fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    override suspend fun <T> retryWithExponentialBackoff(
        times: Int,
        initialDelay: Long,
        factor: Double,
        block: suspend () -> T
    ): T? {
        var currentDelay = initialDelay
        repeat(times - 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                Log.e(
                    "retryWithExponentialBackoff()",
                    "Попытка ${attempt + 1} неудачна: ${e.printStackTrace()}"
                )
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong()
        }
        return try {
            block()
        } catch (e: Exception) {
            Log.e(
                "retryWithExponentialBackoff()",
                "Последняя попытка неудачна: ${e.printStackTrace()}"
            )
            null
        }
    }
}

interface NetworkConnectivity {
    fun isConnected(): Boolean
    suspend fun <T> retryWithExponentialBackoff(
        times: Int = 3,
        initialDelay: Long = 500,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T?
}