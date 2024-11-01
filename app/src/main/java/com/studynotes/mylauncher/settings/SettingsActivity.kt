package com.studynotes.mylauncher.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
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

        setUpAboutAppCard(this)

        bindingSwitchIconSettingItem(
            switchIconSettingItemBinding = binding.autoWallpaperSetting,
            iconRes = R.drawable.ic_wallpaper,
            titleTxt = "Auto wallpaper",
            keyName = SharedPrefsConstants.KEY_AUTO_WALLPAPER,
            context = this
        ) {
            toggleWallpaperState(it, this, SharedPrefsConstants.KEY_AUTO_WALLPAPER)
        }

        bindingSwitchIconSettingItem(
            switchIconSettingItemBinding = binding.focusSetting,
            iconRes = R.drawable.icon_focus,
            titleTxt = "Focus Mode",
            keyName = SharedPrefsConstants.KEY_FOCUS_MODE,
            context = this,
        ) {
            toggleWallpaperState(false, this, SharedPrefsConstants.KEY_AUTO_WALLPAPER)
            setUpFocusMode(it)
        }

        bindingMoreOptionsLayout(
            moreOptionsLayoutBinding = binding.selectAddictiveApps,
            textRes = "Select Addictive Apps"
        ) {}

        bindingMoreOptionsLayout(
            moreOptionsLayoutBinding = binding.selectColorTheme,
            textRes = "Select Color Theme"
        ) {}
    }

    private fun setUpOnClick() {

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

    private fun bindingMoreOptionsLayout(
        moreOptionsLayoutBinding: MoreOptionsLayoutBinding,
        textRes: String,
        onClick: () -> Unit
    ) {
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
        }
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