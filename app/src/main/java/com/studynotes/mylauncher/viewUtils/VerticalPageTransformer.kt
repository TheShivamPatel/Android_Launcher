package com.studynotes.mylauncher.viewUtils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class VerticalPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        when {
            position < -1 -> {
                view.alpha = 0f
            }
            position <= 1 -> {
                view.alpha = 1 - Math.abs(position)
                view.translationY = position * view.height
            }
            else -> {
                view.alpha = 0f
            }
        }
    }
}
