package com.studynotes.mylauncher.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

    lateinit var binding: BottomSheetAppDrawerSettingsBinding

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
    }


    private fun setUpLayoutMode() {

        context?.let {

            when (BasePreferenceManager.getString(
                it,
                SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT
            )) {
                AppDrawerLayout.LINEAR_LAYOUT.toString() -> binding.simpleListRadioButton.isChecked = true
                AppDrawerLayout.GRID_LAYOUT.toString() -> binding.gridListRadioButton.isChecked = true
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

    override fun onStart() {
        super.onStart()
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            val layoutParams = sheet.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(20, 0, 20, 20)
            sheet.layoutParams = layoutParams
        }
    }


}
