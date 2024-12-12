package com.studynotes.mylauncher.fragments.feeds.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.studynotes.mylauncher.baseNetwork.Resource
import com.studynotes.mylauncher.fragments.feeds.model.NewsResponse
import com.studynotes.mylauncher.fragments.feeds.repository.FeedsRepository
import com.studynotes.mylauncher.utils.ApiResultType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class NewsFeedViewModel(private val feedsRepository: FeedsRepository) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext + Dispatchers.Default

    var fetchTopNewsList = MutableLiveData<NewsResponse?>()
    val overlayLD = MutableLiveData<ApiResultType>()

    private var newsFeedsJob: Job? = null

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                // please log on firebase
            }
        }

    fun fetchTopNews(country: String, page: Int) {
        if (newsFeedsJob?.isActive == true) newsFeedsJob?.cancel()
        newsFeedsJob = launch(coroutineExceptionHandler + Dispatchers.IO) {
            val response = feedsRepository.fetchTopNews(country, page)
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        fetchTopNewsList.postValue(it)
                    } ?: kotlin.run {
                        overlayLD.postValue(ApiResultType.TYPE_SUCCESS)
                    }
                }

                Resource.Status.LOADING -> {
                    overlayLD.postValue(ApiResultType.TYPE_LOADING)
                }

                Resource.Status.ERROR -> {
                    overlayLD.postValue(ApiResultType.TYPE_ERROR)
                }
            }
        }
    }

    class Factory(
        private val repo: FeedsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NewsFeedViewModel(repo) as T
        }
    }
}