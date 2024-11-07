package com.studynotes.mylauncher.settings

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.ActivitySettingsBinding
import com.studynotes.mylauncher.databinding.HorizontalIconTextItemBinding
import com.studynotes.mylauncher.databinding.MoreOptionsLayoutBinding
import com.studynotes.mylauncher.databinding.SwitchIconSettingItemBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerLayout
import com.studynotes.mylauncher.mainActivity.MainActivity
import com.studynotes.mylauncher.popUpFragments.themeColorPicker.ThemeColorPickerBottomSheet
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.specialApps.SpecialAppsActivity
import com.studynotes.mylauncher.utils.TimeBase
import com.studynotes.mylauncher.utils.getCurrentTime
import com.studynotes.mylauncher.utils.openAppOnPlayStore
import com.studynotes.mylauncher.utils.openBrowser
import com.studynotes.mylauncher.utils.shareImageFromDrawable
import com.studynotes.mylauncher.viewUtils.ViewUtils
import com.studynotes.mylauncher.viewUtils.ViewUtils.setStatusBarColor

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var wallpaperState: Boolean = false
    private var backgroundColor : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        getFocusBgColor()
        setUpViews()
        setUpStatusBar()
    }

    private fun setUpViews() {
        wallpaperState = BasePreferenceManager.getBoolean(this, SharedPrefsConstants.KEY_AUTO_WALLPAPER, false)
        setUpWallPaper(wallpaperState)

        setUpAboutAppCard(this)

        bindingSwitchIconSettingItem(
            switchIconSettingItemBinding = binding.autoWallpaperSetting,
            iconRes = R.drawable.ic_wallpaper,
            titleTxt = "Auto wallpaper",
            subTitleTxt = getString(R.string.wallpaper_option_desc),
            keyName = SharedPrefsConstants.KEY_AUTO_WALLPAPER,
            context = this
        ) {
            toggleWallpaperState(it, this, SharedPrefsConstants.KEY_AUTO_WALLPAPER)
        }

        bindingSwitchIconSettingItem(
            switchIconSettingItemBinding = binding.focusSetting,
            iconRes = R.drawable.icon_focus,
            titleTxt = "Focus Mode",
            subTitleTxt = "A distraction-free environment for focused work.",
            keyName = SharedPrefsConstants.KEY_FOCUS_MODE,
            context = this,
        ) {
            toggleWallpaperState(false, this, SharedPrefsConstants.KEY_AUTO_WALLPAPER)
            setUpFocusMode(it)
        }

        bindingMoreOptionsLayout(
            moreOptionsLayoutBinding = binding.selectChangeLauncher,
            iconRes = R.drawable.ic_home,
            textRes = "Change Launcher"
        ) {
            startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
        }

        bindingMoreOptionsLayout(
            moreOptionsLayoutBinding = binding.selectAddictiveApps,
            iconRes = R.drawable.icon_hide,
            textRes = getString(R.string.select_addictive_apps)
        ) {
            startActivity(Intent(this, SpecialAppsActivity::class.java))
        }

        bindingMoreOptionsLayout(
            moreOptionsLayoutBinding = binding.selectColorTheme,
            iconRes = R.drawable.icon_color_theme,
            textRes = getString(R.string.select_color_theme)
        ) {
            ThemeColorPickerBottomSheet{ backgroundColor = it
                BasePreferenceManager.putString(this, SharedPrefsConstants.KEY_FOCUS_MODE_BG_COLOR, "0xFF0B0B0B")
                toggleWallpaperState(false, this, SharedPrefsConstants.KEY_AUTO_WALLPAPER)
            }.show(supportFragmentManager, "ThemeColorPickerBottomSheet" )
        }
    }

    private fun bindingSwitchIconSettingItem(
        switchIconSettingItemBinding: SwitchIconSettingItemBinding,
        iconRes: Int,
        titleTxt: String,
        subTitleTxt: String,
        keyName: String,
        context: Context,
        onClick: (isChecked: Boolean) -> Unit
    ) {
        switchIconSettingItemBinding.apply {
            title.text = titleTxt
            leadingIcon.setImageDrawable(
                ContextCompat.getDrawable(context, iconRes)
            )
            if(subTitleTxt.isNotBlank()){
                tvSubtitle.visibility = View.VISIBLE
                tvSubtitle.text = subTitleTxt
            }
            val switchState = BasePreferenceManager.getBoolean(context, keyName, false)
            swOnOff.isChecked = switchState
            swOnOff.setOnCheckedChangeListener { _, isChecked ->
                onClick(isChecked)
            }
        }
    }

    private fun bindingMoreOptionsLayout(
        moreOptionsLayoutBinding: MoreOptionsLayoutBinding,
        textRes: String,
        iconRes: Int,
        onClick: () -> Unit
    ) {
        moreOptionsLayoutBinding.leadingIcon.setImageDrawable(ContextCompat.getDrawable(this, iconRes))
        moreOptionsLayoutBinding.title.text = textRes
        moreOptionsLayoutBinding.root.setOnClickListener { onClick() }
    }

    private fun setUpFocusMode(isChecked: Boolean) {
        if (isChecked) {
            val backgroundColor = ContextCompat.getColor(this, R.color.black)
            binding.root.setBackgroundColor(backgroundColor)
            BasePreferenceManager.putString(this, SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT, AppDrawerLayout.LINEAR_LAYOUT.toString())
        } else {
            val backgroundColor = ContextCompat.getColor(this, R.color.transparent)
            binding.root.setBackgroundColor(backgroundColor)
        }
        BasePreferenceManager.putBoolean(this, SharedPrefsConstants.KEY_FOCUS_MODE, isChecked)
    }


    private fun toggleWallpaperState(currentState: Boolean, context: Context, keyName: String) {
        BasePreferenceManager.putBoolean(context, keyName, currentState)
        setUpWallPaper(currentState)
    }

    private fun setUpWallPaper(wallpaperState: Boolean) {

        val focusModeState = BasePreferenceManager.getBoolean(this, SharedPrefsConstants.KEY_FOCUS_MODE, false)
        val backgroundColor = ContextCompat.getColor(this, R.color.black)
        if (focusModeState) {
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
        }
    }

    private fun getFocusBgColor(): Int {
        var backgroundColor : Int? = 0
        val bgState = BasePreferenceManager.getString(this, SharedPrefsConstants.KEY_FOCUS_MODE_BG_COLOR)
        backgroundColor = if (bgState.isNullOrEmpty()) {
            ContextCompat.getColor(this, R.color.black)
        } else {
            try {
                Color.parseColor(bgState)
            } catch (e: IllegalArgumentException) {
                ContextCompat.getColor(this, R.color.black)
            }
        }
        return backgroundColor as Int
    }

    private fun setUpToolbar() {
        binding.toolbar.toolbarBackIcon.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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

    private fun setUpAboutAppCard(context: Context) {
        bindAboutCardItem(
            binding.review,
            R.drawable.icon_review,
            getString(R.string.review),
            context,
            ::reviewAction
        )

        bindAboutCardItem(
            binding.shareApp,
            R.drawable.icon_share,
            getString(R.string.share_app),
            context,
            ::shareAppAction
        )

        bindAboutCardItem(
            binding.tnc,
            R.drawable.icon_tnc,
            getString(R.string.tnc),
            context,
            ::tncAction
        )

        bindAboutCardItem(
            binding.aboutUs,
            R.drawable.icon_person,
            getString(R.string.about_us),
            context,
            ::aboutUsAction
        )

        bindAboutCardItem(
            binding.report,
            R.drawable.icon_report,
            getString(R.string.report),
            context,
            ::reportAction
        )
    }

    private fun bindAboutCardItem(
        layoutInfoAndTermsItemBinding: HorizontalIconTextItemBinding,
        iconRes: Int,
        titleTxt: String,
        context: Context,
        clickAction: () -> Unit
    ) {
        layoutInfoAndTermsItemBinding.title.text = titleTxt
        layoutInfoAndTermsItemBinding.leadingIcon.setImageDrawable(
            ContextCompat.getDrawable(context, iconRes)
        )
        layoutInfoAndTermsItemBinding.root.setOnClickListener {
            clickAction.invoke()
        }
    }

    private fun aboutUsAction() {
        openBrowser(this, "")
    }

    private fun shareAppAction() {
        shareImageFromDrawable(
            context = this@SettingsActivity,
            R.drawable.ic_wallpaper,
            "Download the app now!"
        )
    }

    private fun reviewAction() {
        openAppOnPlayStore(this, "")
    }

    private fun tncAction() {
        openBrowser(this, "")
    }

    private fun reportAction() {
        openAppOnPlayStore(this, "")
    }

}