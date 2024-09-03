package com.bhaakl.newsapp.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.bhaakl.newsapp.presentation.model.UiLoadState
import com.bhaakl.newsapp.data.util.error.UNEXPECTED_ERROR
import com.bhaakl.newsapp.domain.usecase.errors.ErrorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager

    /**
     * Creates [MutableLiveData] with [UiLoadState] and the given initial state [UiLoadState.Idle]
     */
//    protected fun <T> MutableLiveData() = MutableLiveData<Resource<T>>(Resource.Idle())

    /**
     * Reset [MutableLiveData] to [UiLoadState.Idle]
     */
    /*fun <T> MutableLiveData<UiLoadState<T>>.reset() {
        value = UiLoadState.Idle()
    }*/

    /**
     * Collect network request and return [UiLoadState] depending request result
     */
    protected fun <T, S> Flow<UiLoadState<T>>.collectRequest(
        state: MutableLiveData<UiLoadState<S>>,
        mappedData: (T) -> S
    ) {
        viewModelScope.launch {
            state.value = UiLoadState.Loading()
            this@collectRequest.collect {
                when (it) {
                    is UiLoadState.Loading -> state.value
                    is UiLoadState.DataError -> state.value = if (it.errorCode != null) UiLoadState.DataError(
                        it.errorCode!!
                    ) else null
                    is UiLoadState.Success -> state.value =
                        if (it.data != null) UiLoadState.Success(mappedData(it.data)) else null
                }
            }
        }
    }

    /**
     * Collect paging request
     */
    protected fun <T : Any, S : Any> Flow<PagingData<T>>.collectPagingRequest(
        mappedData: (T) -> S
    ) = map { pagingData ->
        pagingData.map { data ->
            withContext(Dispatchers.Default) {
                mappedData(data)
            }
        }
    }.cachedIn(viewModelScope)

    protected fun <T : Any, S : Any> Flow<PagingData<T>>.collectPagingRequest(
        liveData: MutableLiveData<UiLoadState<PagingData<S>>>,
        mappedData: (T) -> S
    ) {
        viewModelScope.launch {
            liveData.value = UiLoadState.Loading()
            try {
                this@collectPagingRequest
                    .map { it.map { data -> mappedData(data) } }
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        liveData.value = UiLoadState.Success(pagingData)
                    }
            } catch (e: Exception) {
                liveData.value = UiLoadState.DataError(UNEXPECTED_ERROR)
            }
        }
    }

}