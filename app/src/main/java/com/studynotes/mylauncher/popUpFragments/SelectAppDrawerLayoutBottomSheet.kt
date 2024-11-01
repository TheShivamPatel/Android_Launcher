package com.studynotes.mylauncher.popUpFragments

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
    }

    private fun setUpLayoutMode() {

        context?.let {

            when (BasePreferenceManager.getString(
                it,
                SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT
            )) {
                AppDrawerLayout.LINEAR_LAYOUT.toString() -> binding.simpleListRadioButton.isChecked =
                    true

                AppDrawerLayout.GRID_LAYOUT.toString() -> binding.gridListRadioButton.isChecked =
                    true

                else -> binding.simpleListRadioButton.isChecked = true
            }

            binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.simpleListRadioButton -> {
                        BasePreferenceManager.putString(
                            it,
                            SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT,
                            AppDrawerLayout.LINEAR_LAYOUT.toString()
                        )
                        listener.onLayoutSelected(layoutType = AppDrawerLayout.LINEAR_LAYOUT.toString())
                        dismiss()
                    }

                    R.id.gridListRadioButton -> {
                        BasePreferenceManager.putString(
                            it,
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
