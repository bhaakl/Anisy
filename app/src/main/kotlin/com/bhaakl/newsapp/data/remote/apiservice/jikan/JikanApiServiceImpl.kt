package com.bhaakl.newsapp.data.remote.apiservice.jikan

import android.util.Log
import com.bhaakl.newsapp.data.remote.dto.anime.AnimeDto
import com.bhaakl.newsapp.data.remote.dto.anime.AnimePagingResponse
import com.bhaakl.newsapp.data.util.error.Error
import com.bhaakl.newsapp.data.util.error.NO_INTERNET_CONNECTION
import com.bhaakl.newsapp.data.remote.ServiceGenerator
import com.bhaakl.newsapp.domain.util.NetworkConnectivity
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

class JikanApiServiceImpl @Inject constructor(
    private val serviceGenerator: ServiceGenerator,
    private val networkConnectivity: NetworkConnectivity
) : JikanApiService {

    override suspend fun requestTopAnime(
        type: String?,
        filter: String?,
        rating: String?,
        sfw: Boolean?,
        page: Int?,
        limit: Int?
    ): Response<AnimePagingResponse<AnimeDto>> {
        val jikanApiService = serviceGenerator.createService(JikanApiService::class.java)

        if (!networkConnectivity.isConnected()) {
            val error = Error(Exception("No internet connection"))
            Log.e("JikanApiService error -> ", error.toString())
            return Response.error(NO_INTERNET_CONNECTION, createErrorResponseBody(error))
        }

        return jikanApiService.requestTopAnime(type, filter, rating, sfw, page, limit)
        /*return try {
            response
        } catch (e: IOException) {
            val ebody = createErrorResponseBody(Error(e))
            Log.d("newsList ioe -> ", ebody.string())
            Response.error(response.code(), ebody)
        } catch (e: Exception) {
            val ebody = createErrorResponseBody(Error(e))
            Log.d("newsList e -> ", ebody.string())
            Response.error(response.code(), ebody)
        }*/
    }

    override suspend fun requestScheduleAnime(
        filter: String?,
        sfw: Boolean?,
        unapproved: Boolean?,
        page: Int?,
        limit: Int?
    ): Response<AnimePagingResponse<AnimeDto>> {
        val jikanApiService = serviceGenerator.createService(JikanApiService::class.java)

        if (!networkConnectivity.isConnected()) {
            val error = Error(Exception("No internet connection"))
            Log.e("JikanApiService error -> ", error.description)
            return Response.error(NO_INTERNET_CONNECTION, createErrorResponseBody(error))
        }

        return jikanApiService.requestScheduleAnime(filter, sfw, unapproved, page, limit)
    }

    private fun createErrorResponseBody(error: Error): ResponseBody {
        val mediaType = "application/json".toMediaType()
        return Json.encodeToString(Error.serializer(), error).toResponseBody(mediaType)
    }
}
