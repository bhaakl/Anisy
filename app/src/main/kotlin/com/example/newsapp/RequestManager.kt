package com.example.newsapp

import android.content.Context
import android.widget.Toast
import com.example.newsapp.models.NewsApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class RequestManager(var context: Context) {
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getNewsHeadlines(listener: OnFetchDataListener<*>, category: String?, query: String?) {
        val callNewsApi = retrofit.create(CallNewsApi::class.java)
        val call =
            callNewsApi.callHeadlines("in", category, query, context.getString(R.string.api_key))

        //Log.i("hello",category);
        try {
            call.enqueue(object : Callback<NewsApiResponse> {
                override fun onResponse(
                    call: Call<NewsApiResponse>,
                    response: Response<NewsApiResponse>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(context, "Проверьте подключение к интернету!", Toast.LENGTH_LONG)
                            .show()
                    }
                    listener.onFetchData(response.body()?.articles ?: emptyList(), response.message())
                }

                override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                    listener.onError("Запрос не удался!")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface CallNewsApi {
        @GET("top-headlines")
        fun callHeadlines( //@Query("sources") String sources,
            @Query("country") country: String?,
            @Query("category") category: String?,
            @Query("q") query: String?,
            @Query("apiKey") api_key: String?
        ): Call<NewsApiResponse>
    }
}