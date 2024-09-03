package com.bhaakl.newsapp.presentation.ui.fragments.details

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bhaakl.newsapp.BuildConfig
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.presentation.model.UiLoadState
import com.bhaakl.newsapp.presentation.base.BaseViewModel
import com.bhaakl.newsapp.presentation.model.AnimeUi
import com.bhaakl.newsapp.presentation.model.MediaItem
import com.bhaakl.newsapp.presentation.model.MediaType
import com.bhaakl.newsapp.domain.util.SingleEvent
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Thumbnail
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
open class DetailsViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    BaseViewModel() {
    private val _galleryImgLoadingState =
        MutableLiveData<UiLoadState<List<Pair<Bitmap, MediaType>>>>()
    val galleryImgLoadingState: LiveData<UiLoadState<List<Pair<Bitmap, MediaType>>>> =
        _galleryImgLoadingState

    //    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _animeData = MutableLiveData<AnimeUi>()
    val animeData: MutableLiveData<AnimeUi> get() = _animeData

    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    //    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val isFavouritePrivate = MutableLiveData<UiLoadState<Boolean>>()
    val isFavourite: LiveData<UiLoadState<Boolean>> get() = isFavouritePrivate

    fun initIntentData(anime: AnimeUi) {
        _animeData.value = anime
    }

    fun loadGalleryTabThumbs(medias: List<MediaItem>, smallImg: String?) {
        val listBitmap: ArrayList<Pair<Bitmap, MediaType>> = ArrayList()
        viewModelScope.launch(Dispatchers.IO) {
            _galleryImgLoadingState.postValue(UiLoadState.Loading())
            for (mediaItem in medias) {
                when (mediaItem.type) {
                    MediaType.IMAGE -> {
                        try {
                            val bitmap =
                                Picasso.get().load(smallImg ?: mediaItem.url).resize(52, 64)
                                    .centerCrop().placeholder(R.drawable.not_available)
                                    .error(R.drawable.not_available).get()
                            listBitmap.add(Pair(bitmap, mediaItem.type))
                            _galleryImgLoadingState.postValue(UiLoadState.Success(listBitmap))
                        } catch (e: IOException) {
                            val error = errorManager.getError(e)
                            _galleryImgLoadingState.postValue(
                                UiLoadState.DataError(
                                    null, error.description
                                )
                            )
                            Log.e("DetailsViewModel", "IOException: ${e.message}")
                        } catch (e: Exception) {
                            val error = errorManager.getError(e)
                            _galleryImgLoadingState.postValue(
                                UiLoadState.DataError(
                                    null, error.description
                                )
                            )
                            Log.e("DetailsViewModel", "UnknownException: ${e.message}")
                        }
                    }

                    MediaType.VIDEO -> {
                        try {
                            val youtube = YouTube.Builder(
                                GoogleNetHttpTransport.newTrustedTransport(),
                                GsonFactory.getDefaultInstance(),
                                null
                            ).setApplicationName("AnisyDetailsGallery").build()

                            val videoId: String = mediaItem.url
                            val videoListResponse =
                                youtube.videos().list(listOf("snippet")).setId(listOf(videoId))
                                    .setKey(BuildConfig.YOUTUBE_API_KEY).execute()

                            val thumbnail: Thumbnail? =
                                videoListResponse.items?.getOrNull(0)?.snippet?.thumbnails?.default

                            val bitmap = thumbnail?.url?.let { thumbnailUrl ->
                                Picasso.get().load(thumbnailUrl).resize(52, 64).centerCrop().get()
                            }

                            if (bitmap != null) {
                                listBitmap.add(Pair(bitmap, mediaItem.type))
                                _galleryImgLoadingState.postValue(UiLoadState.Success(listBitmap))
                            }
                        } catch (e: GoogleJsonResponseException) {
                            val error = errorManager.getError(e)
                            _galleryImgLoadingState.postValue(
                                UiLoadState.DataError(
                                    null, error.description
                                )
                            )
                            Log.e(
                                "DetailsViewModel",
                                "YouTube API Error: ${e.details.code} - ${e.details.message}"
                            )
                        } catch (e: IOException) {
                            val error = errorManager.getError(e)
                            _galleryImgLoadingState.postValue(
                                UiLoadState.DataError(
                                    null, error.description
                                )
                            )
                            Log.e("DetailsViewModel", "Network Error: ${e.message}")
                        } catch (e: IllegalArgumentException) {
                            val error = errorManager.getError(e)
                            _galleryImgLoadingState.postValue(
                                UiLoadState.DataError(
                                    null, error.description
                                )
                            )
                            Log.e("DetailsViewModel", "Invalid Argument: ${e.message}")
                        } catch (e: Exception) {
                            val error = errorManager.getError(e)
                            _galleryImgLoadingState.postValue(
                                UiLoadState.DataError(
                                    null, error.description
                                )
                            )
                            Log.e("DetailsViewModel", "UnknownException: ${e.message}")
                        }
                    }

                }
            }
        }
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description + "(ecode: " + error.code + ")")
    }

    fun showSnackBarMessage(error: String?, errorCode: Int? = null) {
        if (error != null) {
            showSnackBarPrivate.value = SingleEvent(error)
            Log.d("DetailsViewModel", "showSnackBarMessage: $error")
        }
        if (errorCode != null) {
            showSnackBarPrivate.value = SingleEvent(errorCode)
            Log.d("DetailsViewModel", "showSnackBarMessage: $errorCode")
        }
    }

}
