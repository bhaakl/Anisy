package com.bhaakl.anisy.data.base

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bhaakl.anisy.data.datasource.remote.dto.anime.AnimePagingResponse
import com.bhaakl.anisy.data.util.Network
import com.bhaakl.anisy.data.util.NetworkConnectivity
import com.bhaakl.anisy.domain.util.mapper.DataMapper
import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val BASE_STARTING_PAGE_INDEX = 1

abstract class BasePagingSource<ValueDto : DataMapper<Value>, Value : Any>(
    context: Context,
    private val request: suspend (page: Int) -> Response<AnimePagingResponse<ValueDto>>,
) : PagingSource<Int, Value>() {

    private val network: NetworkConnectivity = Network(context)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val position = params.key ?: BASE_STARTING_PAGE_INDEX
        delay(500)
        return try {
            network.retryWithExponentialBackoff {
                val response = request(position)
                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d(
                        "BasePagingSource Loading","Loading page: $position, hasNextPage: ${data.pagination.hasNextPage}"
                    )
                    LoadResult.Page(
                        data = data.data.map { it.mapToDomain() },
                        prevKey = if (position == BASE_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (data.pagination.hasNextPage && data.data.isNotEmpty()) position + 1 else null
                    )
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("BasePagingSource", "Error: ${response.code()} - $errorMessage")
                    LoadResult.Error(HttpException(response))
                }
            } ?: LoadResult.Error(Exception("Failed to load data after retries"))
        } catch (e: SocketTimeoutException) {
            Log.e("BasePagingSource", "SocketTimeoutException: ${e.message}")
            LoadResult.Error(e)
        } catch (e: ConnectException) {
            Log.e("BasePagingSource", "ConnectException: ${e.message}")
            LoadResult.Error(e)
        } catch (e: UnknownHostException) {
            Log.e("BasePagingSource", "UnknownHostException: ${e.message}")
            LoadResult.Error(e)
        } catch (e: IOException) {
            Log.e("BasePagingSource", "IOException: ${e.message}")
            LoadResult.Error(e)
        } catch (e: kotlinx.serialization.SerializationException) {
            Log.e("BasePagingSource", "SerializationException: ${e.message}")
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e("BasePagingSource", "Unknown Exception: ${e.message}")
            LoadResult.Error(e)
        } 
    }

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return if (state.pages.isEmpty()) {
            BASE_STARTING_PAGE_INDEX
        } else {
            state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
    }
}