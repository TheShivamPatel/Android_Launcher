package com.studynotes.mylauncher.popUpFragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.BottomSheetAppSettingsBinding
import com.studynotes.mylauncher.databinding.LeadingIconWithTitleDialogItemBinding
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.permissions.GenericPermissionFragment
import com.studynotes.mylauncher.permissions.GenericPermissionListener
import com.studynotes.mylauncher.permissions.isOverlayPermissionGranted
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.roomDB.Dao.HomeAppDao
import com.studynotes.mylauncher.roomDB.Dao.RestrictedAppDao
import com.studynotes.mylauncher.roomDB.Model.HomeApp
import com.studynotes.mylauncher.roomDB.Model.RestrictedApp
import com.studynotes.mylauncher.roomDB.convertors.Convertors
import com.studynotes.mylauncher.roomDB.database.LauncherDatabase
import com.studynotes.mylauncher.viewUtils.ViewUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppSettingsBottomSheet(private val appInfo: AppInfo) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAppSettingsBinding
    private var homeAppDao: HomeAppDao? = null
    private var restrictedAppDao: RestrictedAppDao? = null

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
        homeAppDao = LauncherDatabase.getDatabase(requireContext()).homeAppDao()
        restrictedAppDao = LauncherDatabase.getDatabase(requireContext()).restrictedAppsDao()
        setUpViews()
        setUpOnClick()
    }

    private fun setUpOnClick() {
        binding.imageClose.setOnClickListener { dismiss() }
    }

    private fun setUpViews() {
        binding.tvAppLabel.text = appInfo.label
        binding.imgIcon.setImageDrawable(appInfo.icon)
        context?.let { setUpBottomSheetOptions(it) }
    }

    private fun setUpBottomSheetOptions(context: Context) {

        lifecycleScope.launch {
            val isAppInHome = homeAppDao?.isAppInHome(appInfo.packageName!!) ?: false
            val actionTitle = if (isAppInHome) "Remove from Home" else "Add to Home"
            val homeIcon = if (isAppInHome) R.drawable.ic_home_remove else R.drawable.ic_home

            bindingLeadingIconTitleTile(
                binding.optionAddRemoveHomeApp,
                iconRes = homeIcon,
                titleRes = actionTitle,
                context = context
            ) {
                addRemoveFromHome(isAppInHome, context)
            }
        }

        lifecycleScope.launch {
            val isAppRestricted = restrictedAppDao?.isAppRestricted(appInfo.packageName!!) ?: false
            val actionTitle =
                if (isAppRestricted) "Disable SmartUsage Limits" else "Enable SmartUsage Limits"

            bindingLeadingIconTitleTile(
                binding.optionTimeLimit,
                iconRes = R.drawable.icon_clock,
                titleRes = actionTitle,
                context = context
            ) {
                if(checkFeaturePermission(context)){
                    enableDisableSmartUsage(isAppRestricted, context)
                }else{
                    ViewUtils.showToast(context, "Required Permissions")
                }
            }
        }

        bindingLeadingIconTitleTile(
            binding.optionAppInfo,
            iconRes = R.drawable.ic_info,
            titleRes = "App info",
            context = context,
            onClick = ::openAppInfo
        )
    }

    private fun bindingLeadingIconTitleTile(
        tile: LeadingIconWithTitleDialogItemBinding,
        iconRes: Int,
        titleRes: String,
        context: Context,
        onClick: () -> Unit
    ) {
        tile.tvTitle.text = titleRes
        tile.leadingIcon.setImageDrawable(ContextCompat.getDrawable(context, iconRes))
        tile.root.setOnClickListener { onClick() }
    }

    private fun addRemoveFromHome(isAppInHome: Boolean, context: Context) {

        CoroutineScope(Dispatchers.IO).launch {
            val homeAppCount = homeAppDao?.getHomeAppCount() ?: 0

            if (!isAppInHome && homeAppCount >= 6) {
                withContext(Dispatchers.Main) {
                    ViewUtils.showToast(
                        context,
                        "You can only add up to 6 apps to the Home screen."
                    )
                }
            } else {
                if (isAppInHome) {
                    homeAppDao?.deleteHomeAppByPackageName(packageName = appInfo.packageName!!)
                } else {
                    homeAppDao?.insertHomeApp(
                        HomeApp(
                            label = appInfo.label,
                            packageName = appInfo.packageName ?: "",
                            iconData = Convertors().fromDrawableToByteArray(appInfo.icon)
                        )
                    )
                    withContext(Dispatchers.Main) { ViewUtils.showToast(context, "Added to Home") }
                }
                dismiss()
            }
        }
    }

    private fun enableDisableSmartUsage(isAppRestricted: Boolean, context: Context) {

        CoroutineScope(Dispatchers.IO).launch {

            if (isAppRestricted) {
                restrictedAppDao?.removeFromRestrictedApps(packageName = appInfo.packageName!!)
                withContext(Dispatchers.Main) { ViewUtils.showToast(context, "Disabled") }
            } else {
                restrictedAppDao?.insertRestrictedApp(RestrictedApp(packageName = appInfo.packageName!!))
                withContext(Dispatchers.Main) { ViewUtils.showToast(context, "Enabled") }
            }
            dismiss()
        }
    }

    private fun openAppInfo() {
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

    private fun checkFeaturePermission(context: Context) : Boolean{

        if(!isOverlayPermissionGranted(context)){

            GenericPermissionFragment.showFragment(
                fragmentManager = requireActivity().supportFragmentManager,
                object : GenericPermissionListener {
                    override fun onResume() {
                        checkFeaturePermission(context)
                    }

                    override fun onPermissionDenied() {
                    }

                    override fun onPermissionGranted() {
                        Handler().postDelayed({
                            checkFeaturePermission(context)
                        }, 350L)
                    }
                },
                messageBoxText = getString(R.string.title_request_overlay_permission),
                image = R.drawable.icon_layers,
                positiveButtonText = "Yes, Enable",
                permissionType = GenericPermissionFragment.Companion.GenericPermissionType.OVERLAY
            )
        }
        else{
//            ViewUtils.showToast(context, "All Set!")
            return true
        }
        return false
    }

}