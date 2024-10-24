package com.studynotes.mylauncher.testing.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.adapter.ScreenSlidePagerAdapter
import com.studynotes.mylauncher.databinding.FragmentTestHomeBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerAdapter
import com.studynotes.mylauncher.model.AppInfo
import com.studynotes.mylauncher.viewUtils.FadePageTransformer
import com.studynotes.mylauncher.viewUtils.ViewUtils

class TestHomeFragment : Fragment() {

    private lateinit var binding : FragmentTestHomeBinding
    private var pagerAdapter : ScreenSlidePagerAdapter? = null
    private var adapter: AppDrawerAdapter? = null
    private var appsList: MutableList<AppInfo> = mutableListOf()
    private var packageManager: PackageManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews(){
        setUpViewPager()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        context?.let {
            adapter = AppDrawerAdapter(getHomeAppList(it))
            binding.appsRv.layoutManager = GridLayoutManager(it, 4)
        }
        binding.appsRv.adapter = adapter
    }

    private fun getHomeAppList(context: Context): List<AppInfo> {
        packageManager = context.packageManager

        val homeApps = listOf(
            "com.google.android.dialer",
            "com.google.android.contacts",
            "com.android.camera",
            "com.google.android.googlequicksearchbox"
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

    private fun setUpViewPager() {
        val appCompatActivity = requireActivity() as AppCompatActivity
        pagerAdapter = ScreenSlidePagerAdapter(appCompatActivity)
        binding.testHorizontalViewpager.adapter = pagerAdapter
        binding.testHorizontalViewpager.setCurrentItem(1, false)
        binding.testHorizontalViewpager.setPageTransformer(FadePageTransformer())
    }

}