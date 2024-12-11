package com.studynotes.mylauncher.mainActivity

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.mainActivity.adapter.ScreenSlidePagerAdapter
import com.studynotes.mylauncher.databinding.ActivityMainBinding
import com.studynotes.mylauncher.popUpFragments.TitleSubtitleBottomSheet
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.roomDB.database.LauncherDatabase
import com.studynotes.mylauncher.utils.TimeBase
import com.studynotes.mylauncher.utils.getCurrentTime
import com.studynotes.mylauncher.viewUtils.FadePageTransformer
import com.studynotes.mylauncher.viewUtils.ViewUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private var pagerAdapter: ScreenSlidePagerAdapter? = null
    private var wallpaperState: Boolean = false
    private var database: LauncherDatabase? = null
    private val adapter by lazy {
        ScreenSlidePagerAdapter(supportFragmentManager)
    }
    private var currentTabSelectedPosition = 0
    private val pageSelectedListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            currentTabSelectedPosition = position
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpStatusBar()
//        setUpViews()
        setUpStatusBar()
        setUpRoomDB()
        showLauncherSelectionIfNotDefault()
        setUpTabs()
        setUpBottomNavigationMenu()
    }

    private fun setUpStatusBar() {
        ViewUtils.setTransparentNavigationBar(this)
        ViewUtils.setUpStatusBar(this, false)
        ViewUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.transparent))
    }

    private fun showLauncherSelectionIfNotDefault() {
        if (!isCurrentLauncherDefault()) {
            TitleSubtitleBottomSheet().show(supportFragmentManager, "TitleSubtitleBottomSheet")
        }
    }

    private fun isCurrentLauncherDefault(): Boolean {
        val pm = this.packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName == this.packageName
    }

    private fun setUpRoomDB() {
        database = LauncherDatabase.getDatabase(this)
    }

//    private fun setUpViews() {
//        setUpViewPager()
//        wallpaperState =
//            BasePreferenceManager.getBoolean(this, SharedPrefsConstants.KEY_AUTO_WALLPAPER, false)
//        setUpWallPaper(wallpaperState)
//    }

    fun setUpWallPaper(wallpaperState: Boolean) {
        val focusModeState =
            BasePreferenceManager.getBoolean(this, SharedPrefsConstants.KEY_FOCUS_MODE, false)
        if (focusModeState) {
            val backgroundColor = ContextCompat.getColor(this, R.color.black)
            binding.root.setBackgroundColor(backgroundColor)
            binding.mainWallpaper.visibility = View.GONE
        } else {

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
            val backgroundColor = ContextCompat.getColor(this, R.color.transparent)
            binding.root.setBackgroundColor(backgroundColor)
        }
    }

//    private fun setUpViewPager() {
//        pagerAdapter = ScreenSlidePagerAdapter(this)
//        binding.viewPager.adapter = pagerAdapter
//        binding.viewPager.setCurrentItem(0, false)
//        binding.viewPager.setPageTransformer(FadePageTransformer())
//    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val defaultPageIndex = 0
        if (binding.viewPager.currentItem != defaultPageIndex) {
            binding.viewPager.setCurrentItem(defaultPageIndex, true)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        setUpViews()
//    }

    private fun setUpTabs() {
        binding.viewPager.adapter = adapter
    }

    private fun setUpBottomNavigationMenu() {
        val tabs: List<TabItem> = listOf(
            TabItem(
                "Home"
            ),
            TabItem(
                "AppDrawer",
                true,
            ),
            TabItem(
                "Feeds",
            ),
        )
        adapter.setItems(tabs)
        binding.viewPager.addOnPageChangeListener(pageSelectedListener)
    }

}