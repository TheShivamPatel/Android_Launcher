package com.studynotes.mylauncher.fragments.featured_widgets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.mainActivity.MainActivity
import com.studynotes.mylauncher.specialApps.SpecialAppsActivity
import com.studynotes.mylauncher.databinding.FragmentFeaturedWidgetBinding
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants

class FeaturedWidgetFragment : Fragment(R.layout.fragment_featured_widget) {

    private lateinit var binding: FragmentFeaturedWidgetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeaturedWidgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOnClick()
        setUpViews()
    }

    private fun setUpViews() {
        setUpWidgetTile()
    }

    private fun setUpWidgetTile() {
        context?.let {
            val wallpaperState = BasePreferenceManager.getBoolean(it, SharedPrefsConstants.KEY_AUTO_WALLPAPER, false)
            updateWallpaperUI(wallpaperState)
        }
    }

    private fun setUpOnClick() {

        binding.callenderWidget.setOnClickListener {
            startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
        }
        binding.wallpaperWidget.setOnClickListener {
            context?.let { it1 ->
                val wallpaperState = BasePreferenceManager.getBoolean(it1, SharedPrefsConstants.KEY_AUTO_WALLPAPER, false)
                toggleWallpaperState(wallpaperState, it1, SharedPrefsConstants.KEY_AUTO_WALLPAPER)
            }
        }
    }

    private fun toggleWallpaperState(currentState: Boolean, context: Context, keyName: String) {
        val newState = !currentState
        BasePreferenceManager.putBoolean(context, keyName, newState)
        updateWallpaperUI(newState)
        (activity as MainActivity).setUpWallPaper(newState)
    }

    private fun updateWallpaperUI(isEnabled: Boolean) {
        binding.wallpaperWidget.isSelected = isEnabled
        val backgroundRes = if (isEnabled) R.drawable.glassmorphism_on else R.drawable.glassmorphism_off
        binding.wallpaperWidget.setBackgroundResource(backgroundRes)
    }

}

