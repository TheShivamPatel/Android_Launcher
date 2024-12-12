package com.studynotes.mylauncher.fragments.feeds

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.baseNetwork.RetrofitHelper
import com.studynotes.mylauncher.databinding.FragmentFeedScreenBinding
import com.studynotes.mylauncher.fragments.feeds.adapter.FeedAdapter
import com.studynotes.mylauncher.fragments.feeds.network.NewsFeedsFetcher
import com.studynotes.mylauncher.fragments.feeds.network.NewsFeedsService
import com.studynotes.mylauncher.fragments.feeds.repository.FeedsRepository
import com.studynotes.mylauncher.fragments.feeds.viewModel.NewsFeedViewModel
import com.studynotes.mylauncher.utils.STATUS_OK
import com.studynotes.mylauncher.viewUtils.ViewUtils

class FeedScreenFragment : Fragment(R.layout.fragment_feed_screen) {

    private lateinit var binding: FragmentFeedScreenBinding
    private var repo: FeedsRepository? = null
    private var fetcher: NewsFeedsFetcher? = null
    private var viewModel: NewsFeedViewModel? = null
    private var feedAdapter: FeedAdapter? = null

    companion object {
        fun newInstance(): FeedScreenFragment {
            return FeedScreenFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupApiCalling()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel?.fetchTopNewsList?.observe(viewLifecycleOwner) { getNews ->
            Log.d("zzz", getNews.toString())
            if (getNews?.status == STATUS_OK) {
                feedAdapter = getNews.let {
                        FeedAdapter(it.articles)
                }
                binding.newsFeedRV.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = feedAdapter
                }
            } else {
                context?.let { ViewUtils.showToast(it, getNews?.status.toString()) }
            }
        }
    }

    private fun setupApiCalling() {
        fetcher = NewsFeedsFetcher(
            RetrofitHelper.createRetrofitService(
                NewsFeedsService::class.java
            )
        )
        repo = FeedsRepository(fetcher as NewsFeedsFetcher)

        repo?.let {
            viewModel = ViewModelProvider(
                this,
                NewsFeedViewModel.Factory(it)
            )[NewsFeedViewModel::class.java]
        }
        viewModel?.fetchTopNews("us", 1)
    }

}
