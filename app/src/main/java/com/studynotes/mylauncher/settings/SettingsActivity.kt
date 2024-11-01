package com.studynotes.mylauncher.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.ActivitySettingsBinding
import com.studynotes.mylauncher.databinding.SwitchIconSettingItemBinding
import com.studynotes.mylauncher.mainActivity.MainActivity.TimeBase
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.viewUtils.ViewUtils
import com.studynotes.mylauncher.viewUtils.ViewUtils.setStatusBarColor
import java.util.Calendar

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var wallpaperState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpViews()
        setUpStatusBar()
        setUpOnClick()
    }

    private fun setUpViews() {
        wallpaperState =
            BasePreferenceManager.getBoolean(this, SharedPrefsConstants.KEY_AUTO_WALLPAPER, false)
        setUpWallPaper(wallpaperState)
    }

    private fun setUpOnClick() {
        bindingSwitchIconSettingItem(
            switchIconSettingItemBinding = binding.autoWallpaperSetting,
            iconRes = R.drawable.ic_wallpaper,
            titleTxt = "Auto wallpaper",
            keyName = SharedPrefsConstants.KEY_AUTO_WALLPAPER,
            context = this
        ){
            toggleWallpaperState(it, this, SharedPrefsConstants.KEY_AUTO_WALLPAPER)
        }
    }

    private fun bindingSwitchIconSettingItem(
        switchIconSettingItemBinding: SwitchIconSettingItemBinding,
        iconRes: Int,
        titleTxt: String,
        keyName: String,
        context: Context,
        onClick: (isChecked: Boolean) -> Unit
    ) {
        switchIconSettingItemBinding.apply {
            title.text = titleTxt
            leadingIcon.setImageDrawable(
                ContextCompat.getDrawable(context, iconRes)
            )
            val switchState = BasePreferenceManager.getBoolean(context, keyName, true)
            swOnOff.isChecked = switchState
            swOnOff.setOnCheckedChangeListener { _, isChecked ->
                onClick(isChecked)
            }
        }
    }

    private fun toggleWallpaperState(currentState: Boolean, context: Context, keyName: String) {
        BasePreferenceManager.putBoolean(context, keyName, currentState)
        setUpWallPaper(currentState)
    }

    private fun setUpWallPaper(wallpaperState: Boolean) {
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

    private fun setUpToolbar() {
        binding.toolbar.toolbarBackIcon.setOnClickListener {
            finish()
        }
        binding.toolbar.toolbarTitle.text = getString(R.string.settings)
        binding.toolbar.toolbarIconMore.visibility = View.GONE
    }

    private fun setUpStatusBar() {
        ViewUtils.setTransparentNavigationBar(this)
        ViewUtils.addLightStatusBar(this@SettingsActivity)
        setStatusBarColor(
            this@SettingsActivity,
            ContextCompat.getColor(this@SettingsActivity, R.color.transparent)
        )
    }

}