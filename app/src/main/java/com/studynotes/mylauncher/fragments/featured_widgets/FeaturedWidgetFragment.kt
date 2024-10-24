package com.studynotes.mylauncher.fragments.featured_widgets

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.activity.mainHome.MainActivity
import com.studynotes.mylauncher.activity.specialApps.SpecialAppsActivity
import com.studynotes.mylauncher.activity.specialApps.adapter.SpecialAppAdapter
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
        val wallpaperTile = binding.wallpaperWidget.background as? GradientDrawable
        setUpWallpaperTile(wallpaperTile, SharedPrefsConstants.KEY_AUTO_WALLPAPER)

        val studyModeTile = binding.studyModeWidget.background as? GradientDrawable
        setUpWallpaperTile(studyModeTile, SharedPrefsConstants.KEY_STUDY_MODE)
    }

    private fun setUpWallpaperTile(background: GradientDrawable?, keyName: String) {
        context?.let { it1 ->
            val wallpaperState = BasePreferenceManager.getBoolean(it1, keyName, false)
            if (wallpaperState) {
                background?.setColor(ContextCompat.getColor(it1, R.color.white))
            } else {
                background?.setColor(ContextCompat.getColor(it1, R.color.primaryCardColor))
            }
        }
    }

    private fun setUpOnClick() {
        binding.studyModeMore.setOnClickListener {
            startActivity(Intent(context, SpecialAppsActivity::class.java))
        }
        binding.wallpaperWidget.setOnClickListener {
            context?.let { it1 ->
                val wallpaperState = BasePreferenceManager.getBoolean(it1, SharedPrefsConstants.KEY_AUTO_WALLPAPER, false)
                setAutoWallpaper(wallpaperState, it1, SharedPrefsConstants.KEY_AUTO_WALLPAPER)
            }
        }
        binding.studyModeWidget.setOnClickListener {
            context?.let { it1 ->
                val studyModeState = BasePreferenceManager.getBoolean(it1, SharedPrefsConstants.KEY_STUDY_MODE, false)
                setStudyMode(studyModeState, it1, SharedPrefsConstants.KEY_STUDY_MODE)
            }
        }
    }

    private fun setStudyMode(currentState: Boolean, context: Context, keyName: String) {
        val background = binding.studyModeWidget.background as? GradientDrawable
        if(currentState){
            BasePreferenceManager.putBoolean(context, keyName, false)
            background?.setColor(ContextCompat.getColor(context, R.color.primaryCardColor))
            binding.onOffTv.text = "OFF"
            binding.onOffTv.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.studyModeMore.background.setTint(ContextCompat.getColor(context, R.color.secondaryCardColor))
        }
        else{
            BasePreferenceManager.putBoolean(context, keyName, true)
            background?.setColor(ContextCompat.getColor(context, R.color.white))
            binding.onOffTv.text = "ON"
            binding.onOffTv.setTextColor(ContextCompat.getColor(context, R.color.primaryCardColor))
            binding.studyModeMore.background.setTint(ContextCompat.getColor(context, R.color.black))
        }
    }

    private fun setAutoWallpaper(currentState: Boolean?, context: Context, keyName: String) {
        val background = binding.wallpaperWidget.background as? GradientDrawable
        if (currentState == true) {
            BasePreferenceManager.putBoolean(context, keyName, false)
            background?.setColor(ContextCompat.getColor(context, R.color.primaryCardColor))
            (activity as MainActivity).setUpWallPaper(false)
        } else {
            BasePreferenceManager.putBoolean(context, keyName, true)
            background?.setColor(ContextCompat.getColor(context, R.color.white))
            (activity as MainActivity).setUpWallPaper(true)
        }
    }
}