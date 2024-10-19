package com.studynotes.mylauncher.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.FragmentAppDrawerBinding

class AppDrawerFragment : Fragment(R.layout.fragment_app_drawer) {

    private lateinit var binding: FragmentAppDrawerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()

    }

    private fun setUpViews() {

    }

}