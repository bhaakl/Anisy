package com.bhaakl.newsapp.presentation.ui.component.newsland

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bhaakl.newsapp.data.DataRepository
import com.bhaakl.newsapp.data.Resource
import com.bhaakl.newsapp.data.UserRepository
import com.bhaakl.newsapp.data.dto.news.Anime
import com.bhaakl.newsapp.data.dto.news.AnimeItem
import com.bhaakl.newsapp.ui.base.BaseViewModel
import com.bhaakl.newsapp.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val userRepo: UserRepository, private val dataRepository: DataRepository) : BaseViewModel() {

    private val animeLiveDataPrivate = MutableLiveData<Resource<Anime>>()
    val animeLiveData: LiveData<Resource<Anime>> get() = animeLiveDataPrivate

    private val openNewsDetailsPrivate = MutableLiveData<SingleEvent<AnimeItem>>()
    val openNewsDetails: LiveData<SingleEvent<AnimeItem>> get() = openNewsDetailsPrivate

    /**
     * Error handling as UI
     */
//    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

//    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    fun getRecipes() {
        viewModelScope.launch {
            animeLiveDataPrivate.value = Resource.Loading()
            dataRepository.requestTopAnime().collect {
                animeLiveDataPrivate.value = it
            }
        }
    }

    fun openNewsDetails(animeItem: AnimeItem) {
        openNewsDetailsPrivate.value = SingleEvent(animeItem)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description + "(ecode: " + error.code + ")")
    }

    // like
    fun toggleLike(email: String) : Boolean {
        var isLiked = false
        viewModelScope.launch {
            isLiked = userRepo.toggleLike(email)
        }
        return isLiked
    }
}