package com.studynotes.mylauncher.fragments.feeds.network

import com.studynotes.mylauncher.fragments.feeds.model.NewsResponse
import retrofit2.Response

class NewsFeedsFetcher(private val service: NewsFeedsService) {

    suspend fun fetchTopNews(country: String, page: Int): Response<NewsResponse> {
        return service.fetchTopNews(country, page)
    }
}