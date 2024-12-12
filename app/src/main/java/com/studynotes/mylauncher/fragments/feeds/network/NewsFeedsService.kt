package com.studynotes.mylauncher.fragments.feeds.network

import com.studynotes.mylauncher.fragments.feeds.model.NewsResponse
import com.studynotes.mylauncher.utils.Constants.Companion.API_KEY
import com.studynotes.mylauncher.utils.Constants.Companion.BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsFeedsService {

    @GET("top-headlines?apiKey=$API_KEY")
    suspend fun fetchTopNews(
        @Query("country") country: String,
        @Query("page") page: Int
    ) : Response<NewsResponse>

    @GET("top-headlines?apiKey=$API_KEY")
    suspend fun getNewsOnCategory(
        @Query("country") country: String,
        @Query("category") category: String ,
        @Query("page") page: Int
    ) : Response<NewsResponse>
}