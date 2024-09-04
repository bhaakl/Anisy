package com.bhaakl.anisy.presentation.ui.fragments.main.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhaakl.anisy.presentation.base.BaseViewModel
import com.bhaakl.anisy.domain.usecase.fetchanime.FetchScheduleAnimeUseCase
import com.bhaakl.anisy.domain.util.SingleEvent
import com.bhaakl.anisy.presentation.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    fetchScheduleAnimeUseCase: FetchScheduleAnimeUseCase
) : BaseViewModel() {
    val animeScheduleFlowDataPaging = fetchScheduleAnimeUseCase().collectPagingRequest { it.toUI() }

    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    //util funs
    fun showSnackBarMessage(error: String?) {
        if (error != null) {
            showSnackBarPrivate.value = SingleEvent(error)
            Log.d("HomeViewModel", "showSnackBarMessage: $error")
        }
    }
}