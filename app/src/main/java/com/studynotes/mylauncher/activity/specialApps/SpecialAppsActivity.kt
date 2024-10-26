package com.studynotes.mylauncher.activity.specialApps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.activity.specialApps.adapter.SpecialAppAdapter
import com.studynotes.mylauncher.activity.specialApps.model.SpecialApp
import com.studynotes.mylauncher.databinding.ActivitySpecialAppsBinding
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.viewUtils.ViewUtils
import com.studynotes.mylauncher.viewUtils.ViewUtils.setStatusBarColor

class SpecialAppsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpecialAppsBinding
    private var adapter: SpecialAppAdapter? = null
    private var packageManager: PackageManager? = null
    private var appsList: MutableList<SpecialApp> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecialAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpStatusBar()
        setUpToolbar()
        setUpViews()
    }

    private fun setUpViews() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        adapter = SpecialAppAdapter(specialAppList = getInstalledAppList(this))
        binding.recyclerViewSpecialApps.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSpecialApps.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInstalledAppList(context: Context): List<SpecialApp> {
        packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val allApps: List<ResolveInfo> = packageManager?.queryIntentActivities(intent, 0)!!

        for (ri in allApps) {
            val appPackageName = ri.activityInfo.packageName
            val app = SpecialApp(
                label = ri.loadLabel(packageManager).toString(),
                packageName = appPackageName,
                icon = ri.activityInfo.loadIcon(packageManager),
                isEnabled = false
            )
            appsList.add(app)
        }
        return appsList
    }

    private fun setUpToolbar() {
        binding.toolbar.toolbarBackIcon.setOnClickListener {
            finish()
        }
        binding.toolbar.toolbarIconMore.visibility = View.GONE
        ViewUtils.setAdaptiveNavigationBarColor(this, getColor(R.color.translucent))
    }

    private fun setUpStatusBar() {
        ViewUtils.addLightStatusBar(this@SpecialAppsActivity)
        setStatusBarColor(
            this@SpecialAppsActivity,
            ContextCompat.getColor(this@SpecialAppsActivity, R.color.primaryCardColor)
        )
    }


}