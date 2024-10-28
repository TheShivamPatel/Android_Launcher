package com.studynotes.mylauncher.mainActivity

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.mainActivity.adapter.ScreenSlidePagerAdapter
import com.studynotes.mylauncher.databinding.ActivityMainBinding
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.viewUtils.FadePageTransformer
import com.studynotes.mylauncher.viewUtils.ViewUtils
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pagerAdapter: ScreenSlidePagerAdapter? = null
    private var wallpaperState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpStatusBar()
        setUpViews()
        setUpStatusBar()
        checkPermission()
    }


    private fun checkPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${this.packageName}")
            )
            this.startActivity(intent)
        } else if (!hasUsageStatsPermission()) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)

        } else {
            ViewUtils.showToast(this, "All Set!")
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun setUpViews() {
        setUpViewPager()
        wallpaperState =
            BasePreferenceManager.getBoolean(this, SharedPrefsConstants.KEY_AUTO_WALLPAPER, false)
        setUpWallPaper(wallpaperState)
    }

    fun setUpWallPaper(wallpaperState: Boolean) {
        if (wallpaperState) {
            val currentTime = getCurrentTime()

            val wallpaperResource = when (currentTime) {
                TimeBase.MORNING -> R.drawable.bg_1
                TimeBase.EVENING -> R.drawable.bg_2
                TimeBase.NIGHT -> R.drawable.bg_3
            }

            binding.mainWallpaper.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    wallpaperResource
                )
            )
            binding.mainWallpaper.visibility = View.VISIBLE
        } else {
            binding.mainWallpaper.visibility = View.GONE
        }
    }

    private fun setUpStatusBar() {
        ViewUtils.setTransparentNavigationBar(this)
        ViewUtils.setUpStatusBar(this, false)
        ViewUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.transparent))
    }

    private fun setUpViewPager() {
        pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setCurrentItem(1, false)
        binding.viewPager.setPageTransformer(FadePageTransformer())
    }


    enum class TimeBase {
        MORNING, EVENING, NIGHT
    }

    private fun getCurrentTime(): TimeBase {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 6..11 -> TimeBase.MORNING
            in 12..17 -> TimeBase.EVENING
            else -> TimeBase.NIGHT
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val defaultPageIndex = 1
        if (binding.viewPager.currentItem != defaultPageIndex) {
            binding.viewPager.setCurrentItem(defaultPageIndex, true)
        }
    }
}