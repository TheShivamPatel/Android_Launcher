package com.studynotes.mylauncher.fragments

import android.app.WallpaperManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.FragmentHomeScreenBinding

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
        setUpOnClick()
    }

    private fun setUpViews() {
        setWallpaperBackground()
    }

    private fun setWallpaperBackground() {

    }

    private fun setUpOnClick() {
        binding.imageBtnDrawer.setOnClickListener {
            loadFragment(fragment = AppDrawerFragment())
        }
    }

    private fun loadFragment(fragment: AppDrawerFragment) {

        fragment.let {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.container, it)
                    .commit()
            }
        }
    }

}