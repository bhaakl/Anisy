package com.bhaakl.anisy.presentation.ui.fragments.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bhaakl.anisy.data.repository.UserRepository
import com.bhaakl.anisy.presentation.base.BaseViewModel
import com.bhaakl.anisy.presentation.model.anime.AnimeUi
import com.bhaakl.anisy.domain.usecase.fetchanime.FetchTopAnimeUseCase
import com.bhaakl.anisy.domain.util.SingleEvent
import com.bhaakl.anisy.presentation.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepo: UserRepository,
    fetchTopAnimeUseCase: FetchTopAnimeUseCase
) : BaseViewModel() {

    val animeFlowDataPaging = fetchTopAnimeUseCase().collectPagingRequest { it.toUI() }
//    val animeLiveData: LiveData<Resource<PagingData<AnimeUi>>> get() = animeLiveDataPrivate

    private val openPostDetailsPrivate = MutableLiveData<SingleEvent<AnimeUi>>()
    val openPostDetails: LiveData<SingleEvent<AnimeUi>> get() = openPostDetailsPrivate

    /**
     * Error handling as UI
     */
//    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    //    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    fun openNewsDetails(anime: AnimeUi) {
        openPostDetailsPrivate.value = SingleEvent(anime)
        Log.d(
            "HomeViewModel",
            "openNewsDetails: ishandled: ${openPostDetails.value?.hasBeenHandled} --> title: ${openPostDetails.value?.peekContent()?.titles?.firstOrNull()?.title}"
        )
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value =
            SingleEvent(error.description + "(ecode: " + error.code + ")")
    }

    fun showSnackBarMessage(error: String?) {
        if (error != null) {
            showSnackBarPrivate.value = SingleEvent(error)
            Log.d("HomeViewModel", "showSnackBarMessage: $error")
        }
    }

    // like
    fun toggleLike(email: String): Boolean {
        var isLiked = false
        viewModelScope.launch {
            isLiked = userRepo.toggleLike(email)
        }
        return isLiked
    }
}