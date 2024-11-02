package com.studynotes.mylauncher.specialApps

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.specialApps.adapter.SpecialAppAdapter
import com.studynotes.mylauncher.specialApps.model.SpecialApp
import com.studynotes.mylauncher.databinding.ActivitySpecialAppsBinding
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.roomDB.Dao.HiddenAppDao
import com.studynotes.mylauncher.roomDB.Dao.RestrictedAppDao
import com.studynotes.mylauncher.roomDB.database.LauncherDatabase
import com.studynotes.mylauncher.viewUtils.ViewUtils
import com.studynotes.mylauncher.viewUtils.ViewUtils.setStatusBarColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpecialAppsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpecialAppsBinding
    private var adapter: SpecialAppAdapter? = null
    private lateinit var hiddenAddictiveAppDao: HiddenAppDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecialAppsBinding.inflate(layoutInflater)
        hiddenAddictiveAppDao = LauncherDatabase.getDatabase(this).hiddenAddictiveAppsDao()
        setContentView(binding.root)
        setUpStatusBar()
        setUpToolbar()
        setUpViews()
    }

    private fun setUpViews() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        lifecycleScope.launch {
            val installedApps = getInstalledAppList(this@SpecialAppsActivity)
            adapter = SpecialAppAdapter(specialAppList = installedApps, this@SpecialAppsActivity, hiddenAppDao = hiddenAddictiveAppDao)
            binding.recyclerViewSpecialApps.layoutManager = LinearLayoutManager(this@SpecialAppsActivity)
            binding.recyclerViewSpecialApps.adapter = adapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getInstalledAppList(context: Context): List<AppInfo> {
        return withContext(Dispatchers.IO) {
            val packageManager = context.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val allApps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

            val appsList = mutableListOf<AppInfo>()

            for (ri in allApps) {
                val appPackageName = ri.activityInfo.packageName
                val app = AppInfo(
                    label = ri.loadLabel(packageManager).toString(),
                    packageName = appPackageName,
                    icon = ri.activityInfo.loadIcon(packageManager)
                )
                appsList.add(app)
            }
            appsList.sortedBy { it.label }
        }
    }

    private fun setUpToolbar() {
        binding.toolbarTitle.text = getString(R.string.select_addictive_apps)
        binding.toolbarBackIcon.setOnClickListener {
            finish()
        }
    }

    private fun setUpStatusBar() {
        ViewUtils.addLightStatusBar(this@SpecialAppsActivity)
        setStatusBarColor(
            this@SpecialAppsActivity,
            ContextCompat.getColor(this@SpecialAppsActivity, R.color.primaryCardColor)
        )
    }

}