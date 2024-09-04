package com.bhaakl.anisy.data.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bhaakl.anisy.domain.model.UiLoadState
import com.bhaakl.anisy.domain.util.NETWORK_ERROR
import com.bhaakl.anisy.domain.util.UNEXPECTED_ERROR
import com.bhaakl.anisy.domain.util.mapper.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository {

    abstract val ioDispatcher: CoroutineContext

    /**
     * Do network request
     *
     * @return result in [flow] with [UiLoadState]
     */
    protected fun <T : DataMapper<S>, S> doNetworkRequest(
        request: suspend () -> Response<T>
    ) = flow<UiLoadState<S>> {
        request().let {
            if (it.isSuccessful && it.body() != null) {
                emit(UiLoadState.Success(it.body()!!.mapToDomain()))
            } else {
                emit(UiLoadState.DataError(NETWORK_ERROR))
            }
        }
    }.flowOn(ioDispatcher).catch { exception ->
        emit(
            UiLoadState.DataError(UNEXPECTED_ERROR)
        )
    }

    /**
     * Convert network error from server side
     */
    private fun ResponseBody?.toApiError(): Map<String, List<String>> {
        return this?.string()?.let { jsonString ->
            Json.decodeFromString(jsonString)
        } ?: emptyMap()
    }

    /**
     * Get non-nullable body from request
     */
    protected inline fun <T : Response<S>, S> T.onSuccess(block: (S) -> Unit): T {
        this.body()?.let(block)
        return this
    }

    /**
     * Do network paging request with default params
     */
    protected fun <ValueDto : DataMapper<Value>, Value : Any> doPagingRequest(
        pagingSource: BasePagingSource<ValueDto, Value>,
        pageSize: Int = 5,    // количество элементов, загружаемых за один раз
        prefetchDistance: Int = pageSize / 2,  // Количество элементов, которые будут предварительно загружены до достижения конца текущей страницы.
        enablePlaceholders: Boolean = true, // Включает/отключает использование плейсхолдеров для элементов, которые еще не загружены.
        initialLoadSize: Int = pageSize,  // Количество элементов, загружаемых при первой загрузке.
        maxSize: Int = pageSize * 50,  // Максимальное количество элементов, которые могут быть загружены.
//        jumpThreshold: Int = Int.MIN_VALUE // Порог, после которого Paging Library будет пытаться "перепрыгнуть" через страницы
    ): Flow<PagingData<Value>> {
        return Pager(
            config = PagingConfig(
                pageSize,
                prefetchDistance,
                enablePlaceholders,
                initialLoadSize,
                maxSize,
//                jumpThreshold
            ),
            pagingSourceFactory = {
                pagingSource
            }
        ).flow
    }
}