package com.studynotes.mylauncher.fragments.feeds.repository

import com.studynotes.mylauncher.baseNetwork.Resource
import com.studynotes.mylauncher.fragments.feeds.model.NewsResponse
import com.studynotes.mylauncher.fragments.feeds.network.NewsFeedsFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedsRepository(private var newsFeedsFetcher: NewsFeedsFetcher) {


    suspend fun fetchTopNews(country: String, page: Int) : Resource<NewsResponse?> {

        return withContext(Dispatchers.IO) {
            try {
                val response = newsFeedsFetcher.fetchTopNews(country, page)
                Resource.success(response.body())
            } catch (e: Exception) {
                Resource.error("something went wrong")
            }
        }
    }
}