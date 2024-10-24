package com.studynotes.mylauncher.fragments.main_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.adapter.ScreenSlidePagerAdapter
import com.studynotes.mylauncher.adapter.VerticalSlidePagerAdapter
import com.studynotes.mylauncher.databinding.FragmentHomeMainBinding
import com.studynotes.mylauncher.databinding.FragmentHomeScreenBinding
import com.studynotes.mylauncher.viewUtils.FadePageTransformer
import com.studynotes.mylauncher.viewUtils.VerticalPageTransformer

class HomeMainFragment : Fragment(R.layout.fragment_home_main) {

    private lateinit var binding: FragmentHomeMainBinding
    private var pagerAdapter : VerticalSlidePagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        setUpViewPager()
    }

    private fun setUpViewPager() {
        pagerAdapter = activity?.let { VerticalSlidePagerAdapter(it) }
        binding.verticalViewPager.adapter = pagerAdapter
        binding.verticalViewPager.setCurrentItem(0, false)
        binding.verticalViewPager.setPageTransformer(VerticalPageTransformer())
    }
}