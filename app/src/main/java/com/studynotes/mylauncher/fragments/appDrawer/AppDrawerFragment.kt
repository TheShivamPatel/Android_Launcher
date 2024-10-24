package com.studynotes.mylauncher.fragments.appDrawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.activity.mainHome.MainActivity
import com.studynotes.mylauncher.bottomSheet.SelectAppDrawerLayoutBottomSheet
import com.studynotes.mylauncher.databinding.FragmentAppDrawerBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerAdapter
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerLayout
import com.studynotes.mylauncher.model.AppInfo
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.viewUtils.ViewUtils

class AppDrawerFragment : Fragment(R.layout.fragment_app_drawer), SelectAppDrawerLayoutBottomSheet.OnLayoutSelectedListener {

    private lateinit var binding: FragmentAppDrawerBinding
    private var adapter: AppDrawerAdapter? = null
    private var packageManager: PackageManager? = null
    private var appsList: MutableList<AppInfo> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            ViewUtils.setTransparentNavigationBar(it)
        }
        setUpViews()
        setUpOnClick()
    }

    private fun setUpOnClick() {
        binding.settingBtn.setOnClickListener {
            SelectAppDrawerLayoutBottomSheet(this).show(
                requireActivity().supportFragmentManager,
                "AppDrawerBottomSheet"
            )
        }
    }

    private fun setUpViews() {
        context?.let {
            val selectedDrawerLayout = BasePreferenceManager.getString(it, SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT,)
            setUpRecyclerView(selectedDrawerLayout)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpRecyclerView(layoutType: String) {
        context?.let {
            adapter = AppDrawerAdapter(getInstalledAppList(it), layoutType)
            if (layoutType == AppDrawerLayout.GRID_LAYOUT.toString()) {
                binding.appsRv.layoutManager = GridLayoutManager(it, 4)
            } else {
                binding.appsRv.layoutManager = LinearLayoutManager(it)
            }
            binding.appsRv.adapter = adapter
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInstalledAppList(context: Context): List<AppInfo> {
        packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val allApps: List<ResolveInfo> = packageManager?.queryIntentActivities(intent, 0)!!

        for (ri in allApps) {
            val appPackageName = ri.activityInfo.packageName
            val app = AppInfo(
                label = ri.loadLabel(packageManager).toString(),
                packageName = appPackageName,
                icon = ri.activityInfo.loadIcon(packageManager)
            )
            Log.i("AppDrawerFragment", "Social app found: ${app.packageName}")
            appsList.add(app)
        }
        return appsList
    }

    override fun onLayoutSelected(layoutType: String) {
        setUpRecyclerView(layoutType)
    }

}
