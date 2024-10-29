package com.studynotes.mylauncher.fragments.home

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.specialApps.SpecialAppsActivity
import com.studynotes.mylauncher.databinding.FragmentHomeScreenBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerAdapter
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerLayout
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.roomDB.Dao.HomeAppDao
import com.studynotes.mylauncher.roomDB.Dao.RestrictedAppDao
import com.studynotes.mylauncher.roomDB.database.LauncherDatabase
import com.studynotes.mylauncher.viewUtils.ViewUtils

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private var adapter: AppDrawerAdapter? = null
    private var appsList: MutableList<AppInfo> = mutableListOf()
    private var homeAppDao: HomeAppDao? = null
    private lateinit var restrictedAppDao: RestrictedAppDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDatabase()
        setUpStatusNavigationBarTheme()
        setUpRecyclerView()
        setUpOnClick()
    }

    private fun setUpDatabase() {
        context?.let {
            homeAppDao = LauncherDatabase.getDatabase(it).homeAppDao()
            restrictedAppDao = LauncherDatabase.getDatabase(it).restrictedAppsDao()
        }
    }

    private fun setUpStatusNavigationBarTheme() {
        activity?.let { ViewUtils.setTransparentNavigationBar(it) }
    }

    private fun setUpOnClick() {
        binding.llAddHomeApps.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    SpecialAppsActivity::class.java
                )
            )
        }
    }


    private fun setUpRecyclerView() {
        binding.recyclerViewHomeApps.layoutManager = LinearLayoutManager(context)

        homeAppDao?.getAllHomeApps()?.observe(viewLifecycleOwner) { homeApps ->
            appsList = homeApps.map { homeApp ->
                AppInfo(homeApp.label, homeApp.packageName, homeApp.iconData?.let { iconData ->
                    BitmapDrawable(
                        resources,
                        BitmapFactory.decodeByteArray(iconData, 0, iconData.size)
                    )
                })
            }.toMutableList()

            context?.let {
                adapter = AppDrawerAdapter(
                    appsList.toList(),
                    AppDrawerLayout.LINEAR_LAYOUT.toString(),
                    it,
                    requireActivity().supportFragmentManager,
                    restrictedAppDao
                )
            }
            binding.recyclerViewHomeApps.adapter = adapter
        }
    }
}