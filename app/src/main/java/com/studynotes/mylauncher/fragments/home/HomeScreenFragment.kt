package com.studynotes.mylauncher.fragments.home

import android.app.ActivityManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.specialApps.SpecialAppsActivity
import com.studynotes.mylauncher.databinding.FragmentHomeScreenBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerAdapter
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerLayout
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.roomDB.Dao.HomeAppDao
import com.studynotes.mylauncher.roomDB.Model.HomeApp
import com.studynotes.mylauncher.roomDB.database.HomeAppDatabase
import com.studynotes.mylauncher.viewUtils.ViewUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private var adapter: AppDrawerAdapter? = null
    private var appsList: MutableList<AppInfo> = mutableListOf()
    private var packageManager: PackageManager? = null
    private lateinit var database : HomeAppDatabase
    private lateinit var homeAppDao: HomeAppDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpStatusNavigationBarTheme()
        setUpRoomDB()
        setUpRecyclerView()
        setUpOnClick()
    }

    private fun setUpRoomDB(){
        // INSTANCE CREATION
        context?.let {
            database = HomeAppDatabase.getDatabase(it) as HomeAppDatabase
            homeAppDao = database.homeAppDao()!!
        }
    }

    private fun setUpStatusNavigationBarTheme() {
        activity?.let { ViewUtils.setTransparentNavigationBar(it) }
    }


    private fun setUpOnClick() {
        binding.llAddHomeApps.setOnClickListener { startActivity(Intent(context, SpecialAppsActivity::class.java)) }
    }

    private fun setUpViews() {

    }

    private fun setUpRecyclerView() {
        context?.let {
            val storedAppList =  getHomeAppList(it)
            adapter = AppDrawerAdapter(storedAppList, AppDrawerLayout.LINEAR_LAYOUT.toString(), it, requireActivity().supportFragmentManager)
        }
        binding.recyclerViewHomeApps.adapter = adapter
        binding.recyclerViewHomeApps.layoutManager = LinearLayoutManager(context)
    }

    private fun getHomeAppList(context: Context): List<AppInfo> {
        packageManager = context.packageManager
        val homeApps = listOf(
            "com.google.android.dialer",
            "com.google.android.contacts",
            "com.android.camera",
            "com.google.android.googlequicksearchbox",
            "com.whatsapp"
        )
        for (packageName in homeApps) {
            try {
                val appInfo = packageManager?.getApplicationInfo(packageName, 0)
                val appName = appInfo?.let { packageManager?.getApplicationLabel(it).toString() }
                val appIcon = packageManager?.getApplicationIcon(packageName)
                appsList.add(AppInfo(appName, packageName, appIcon))
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
        return appsList
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


}