package com.studynotes.mylauncher.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.studynotes.mylauncher.fragments.featured_widgets.FeaturedWidgetFragment
import com.studynotes.mylauncher.fragments.feeds.FeedScreenFragment
import com.studynotes.mylauncher.fragments.home.HomeScreenFragment

class ScreenSlidePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeedScreenFragment()
//            1 -> HomeMainFragment()
            1 -> HomeScreenFragment()
            2 -> FeaturedWidgetFragment()
            else -> HomeScreenFragment()
        }
    }
}