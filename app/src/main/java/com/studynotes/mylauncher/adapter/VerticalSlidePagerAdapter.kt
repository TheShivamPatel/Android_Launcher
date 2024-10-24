package com.studynotes.mylauncher.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.studynotes.mylauncher.fragments.appDrawer.AppDrawerFragment
import com.studynotes.mylauncher.testing.fragments.TestHomeFragment

class VerticalSlidePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> TestHomeFragment()
            1 -> AppDrawerFragment()
            else -> TestHomeFragment()
        }
    }
}