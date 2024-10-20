package com.studynotes.mylauncher.viewUtils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class FadePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.alpha = 1 - kotlin.math.abs(position)
    }
}
