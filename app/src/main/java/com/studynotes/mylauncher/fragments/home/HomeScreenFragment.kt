package com.studynotes.mylauncher.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.FragmentHomeScreenBinding
import com.studynotes.mylauncher.viewUtils.ViewUtils

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpStatusNavigationBarTheme()
    }

    private fun setUpViews() {

    }

    private fun setUpStatusNavigationBarTheme() {
        activity?.let { ViewUtils.setTransparentNavigationBar(it) }
    }



}