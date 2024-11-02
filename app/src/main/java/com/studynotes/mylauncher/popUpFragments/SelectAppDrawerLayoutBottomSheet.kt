package com.studynotes.mylauncher.popUpFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.BottomSheetAppDrawerSettingsBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerLayout
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.settings.SettingsActivity
import com.studynotes.mylauncher.viewUtils.ViewUtils

class SelectAppDrawerLayoutBottomSheet(private val listener: OnLayoutSelectedListener) :
    BottomSheetDialogFragment() {

    interface OnLayoutSelectedListener {
        fun onLayoutSelected(layoutType: String)
    }

    private lateinit var binding: BottomSheetAppDrawerSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAppDrawerSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpLayoutMode()
        setUpOnClick()

    }

    private fun setUpOnClick() {
        binding.imageClose.setOnClickListener { dismiss() }

        binding.llMainSettings.setOnClickListener {
            startActivity(Intent(context, SettingsActivity::class.java))
            dismiss()
        }

    }

    private fun setUpLayoutMode() {

        context?.let {ctx->

            when (BasePreferenceManager.getString(
                ctx,
                SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT
            )) {
                AppDrawerLayout.LINEAR_LAYOUT.toString() -> binding.simpleListRadioButton.isChecked =
                    true

                AppDrawerLayout.GRID_LAYOUT.toString() -> binding.gridListRadioButton.isChecked =
                    true

                else -> binding.simpleListRadioButton.isChecked = true
            }

            binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

                val isFocusModeEnable =  BasePreferenceManager.getBoolean(ctx, SharedPrefsConstants.KEY_FOCUS_MODE, false)
                if(isFocusModeEnable) {
                    ViewUtils.showToast(ctx, "Stay in the zone! Layout options are locked while Focus Mode is enabled.")
                    dismiss()
                    return@setOnCheckedChangeListener
                }
                when (checkedId) {
                    R.id.simpleListRadioButton -> {
                        BasePreferenceManager.putString(
                            ctx,
                            SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT,
                            AppDrawerLayout.LINEAR_LAYOUT.toString()
                        )
                        listener.onLayoutSelected(layoutType = AppDrawerLayout.LINEAR_LAYOUT.toString())
                        dismiss()
                    }

                    R.id.gridListRadioButton -> {
                        BasePreferenceManager.putString(
                            ctx,
                            SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT,
                            AppDrawerLayout.GRID_LAYOUT.toString()
                        )
                        listener.onLayoutSelected(layoutType = AppDrawerLayout.GRID_LAYOUT.toString())
                        dismiss()
                    }
                }
            }

        }
    }
}
