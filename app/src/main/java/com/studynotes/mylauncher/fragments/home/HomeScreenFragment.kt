package com.studynotes.mylauncher.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.FragmentHomeScreenBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerAdapter
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerLayout
import com.studynotes.mylauncher.model.AppInfo
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.viewUtils.ViewUtils

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private var adapter: AppDrawerAdapter? = null


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
        setUpRecyclerView()
    }

    private fun setUpStatusNavigationBarTheme() {
        activity?.let { ViewUtils.setTransparentNavigationBar(it) }
    }

    private fun setUpViews() {

    }

    private fun setUpRecyclerView() {
        val storedAppList = getSpecialAppsListFromDB()
        val homeAppList: List<AppInfo> = storedAppList
        context?.let {
            adapter = AppDrawerAdapter(homeAppList, AppDrawerLayout.LINEAR_LAYOUT.toString(), it, requireActivity().supportFragmentManager)
        }
        binding.recyclerViewHomeApps.adapter = adapter
        binding.recyclerViewHomeApps.layoutManager = LinearLayoutManager(context)
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

    private fun updateSpecialAppsListInDB(specialAppList: List<AppInfo>, context: Context) {
        val gson = Gson()
        val json = gson.toJson(specialAppList)
        BasePreferenceManager.putString(
            context,
            SharedPrefsConstants.KEY_SELECTED_HOME_APPS,
            json
        )
    }

}