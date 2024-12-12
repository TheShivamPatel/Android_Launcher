package com.studynotes.mylauncher.mainActivity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.studynotes.mylauncher.mainActivity.MainPageEvaluator
import com.studynotes.mylauncher.mainActivity.TabItem

class ScreenSlidePagerAdapter(supportFragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val items: MutableList<TabItem> = mutableListOf()

    override fun getCount(): Int {
        return items.size
    }

    fun setItems(list: List<TabItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return MainPageEvaluator.getFragmentBasedOnPageType(items[position])
    }
}