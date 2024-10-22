package com.studynotes.mylauncher.fragments.feeds

import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.FragmentFeedScreenBinding

class FeedScreenFragment : Fragment(R.layout.fragment_feed_screen) {

    private lateinit var binding: FragmentFeedScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyEffectWithBlur()
    }

    private fun applyEffectWithBlur() {

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.argb(255, 100, 64, 68),
                Color.argb(255, 53, 27, 28),
            )
        )
        binding.blurBgLayout.background = gradientDrawable
    }

}
