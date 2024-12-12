package com.studynotes.mylauncher.fragments.feeds.model

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)