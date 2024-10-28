package com.studynotes.mylauncher.bottomSheet

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.studynotes.mylauncher.databinding.BottomSheetAppSettingsBinding
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.viewUtils.ViewUtils

class AppSettingsBottomSheet(private val appInfo: AppInfo) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAppSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAppSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { ViewUtils.setTransparentNavigationBar(it) }
        setUpViews()
        setUpOnClick()
    }

    private fun setUpOnClick() {
        binding.imageClose.setOnClickListener { dismiss() }

        binding.llAddToHome.setOnClickListener {
            updateSpecialAppsListInDB(
                appInfo,
                requireContext()
            )
        }

        binding.llAppInfo.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${appInfo.packageName}")
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                context?.let { it1 -> ViewUtils.showToast(it1, "Unable to open App Info") }
            }
            dismiss()
        }


    }

    private fun setUpViews() {
        binding.tvAppLabel.text = appInfo.label
        binding.imgIcon.setImageDrawable(appInfo.icon)
    }

    private fun updateSpecialAppsListInDB(homeApp: AppInfo, context: Context) {
        val homeAppList: MutableList<AppInfo> = getSpecialAppsListFromDB().toMutableList()
        if (!homeAppList.any { it.packageName == homeApp.packageName }) {
            homeAppList.add(homeApp)
        }
        val gson = Gson()
        val json = gson.toJson(homeAppList)
        BasePreferenceManager.putString(
            context,
            SharedPrefsConstants.KEY_SELECTED_HOME_APPS,
            json
        )
    }


    private fun getSpecialAppsListFromDB(): List<AppInfo> {
        val gson = Gson()
        val json = context?.let {
            BasePreferenceManager.getString(
                context = it,
                SharedPrefsConstants.KEY_SELECTED_HOME_APPS
            )
        }
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            val typeToken = object : TypeToken<ArrayList<AppInfo>>() {}.type
            gson.fromJson(json, typeToken)
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