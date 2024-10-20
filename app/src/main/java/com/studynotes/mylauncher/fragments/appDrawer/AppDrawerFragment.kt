package com.studynotes.mylauncher.fragments.appDrawer

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
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.FragmentAppDrawerBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerAdapter
import com.studynotes.mylauncher.model.AppInfo
import com.studynotes.mylauncher.viewUtils.ViewUtils

class AppDrawerFragment : Fragment(R.layout.fragment_app_drawer) {

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
        applyGlassorphismEffect()
        setUpViews()
    }

    private fun setUpViews() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        context?.let {
            adapter = AppDrawerAdapter(getInstalledAppList(it))
            binding.appsRv.layoutManager = GridLayoutManager(it, 4)
        }
        binding.appsRv.adapter = adapter
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

    private fun applyGlassorphismEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val blurEffect = RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
            binding.blurBgLayout.setRenderEffect(blurEffect)

            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.argb(150, 0, 0, 0), Color.argb(200, 0, 0, 0))
//                intArrayOf(Color.argb(255, 101, 65, 69), Color.argb(255, 54, 28, 29))
            )
            binding.blurBgLayout.background = gradientDrawable
        } else {
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.argb(120, 0, 0, 0), Color.argb(120, 50, 50, 50))
            )
            binding.blurBgLayout.background = gradientDrawable
        }
    }


}