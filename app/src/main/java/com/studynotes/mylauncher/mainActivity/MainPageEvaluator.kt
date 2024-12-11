package com.studynotes.mylauncher.mainActivity

import androidx.fragment.app.Fragment
import com.studynotes.mylauncher.fragments.appDrawer.AppDrawerFragment
import com.studynotes.mylauncher.fragments.feeds.FeedScreenFragment
import com.studynotes.mylauncher.fragments.home.HomeScreenFragment

object MainPageEvaluator {
    private const val PAGE_TYPE_HOME = "Home"
    private const val PAGE_TYPE_APP_DRAWER = "AppDrawer"
    private const val PAGE_TYPE_FEEDS = "Feeds"

    fun getFragmentBasedOnPageType(tab: TabItem): Fragment {
        return when (tab.title) {

            PAGE_TYPE_HOME -> {
                HomeScreenFragment.newInstance()
            }

            PAGE_TYPE_APP_DRAWER -> {
                AppDrawerFragment.newInstance()
            }

            PAGE_TYPE_FEEDS -> {
                FeedScreenFragment.newInstance()
            }

            else -> {
                HomeScreenFragment.newInstance()
            }
        }
    }
}