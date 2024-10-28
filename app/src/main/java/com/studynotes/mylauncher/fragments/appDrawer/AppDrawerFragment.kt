package com.studynotes.mylauncher.fragments.appDrawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.bottomSheet.SelectAppDrawerLayoutBottomSheet
import com.studynotes.mylauncher.databinding.FragmentAppDrawerBinding
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerAdapter
import com.studynotes.mylauncher.fragments.appDrawer.adapter.AppDrawerLayout
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.prefs.BasePreferenceManager
import com.studynotes.mylauncher.prefs.SharedPrefsConstants
import com.studynotes.mylauncher.viewUtils.ViewUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppDrawerFragment : Fragment(R.layout.fragment_app_drawer),
    SelectAppDrawerLayoutBottomSheet.OnLayoutSelectedListener {
    private lateinit var binding: FragmentAppDrawerBinding
    private var adapter: AppDrawerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { ViewUtils.setTransparentNavigationBar(it) }

        setUpSearch()
        setUpViews()
        setUpOnClick()
    }

    private fun setUpOnClick() {
        binding.settingBtn.setOnClickListener {
            activity?.let {
                SelectAppDrawerLayoutBottomSheet(this).show(
                    it.supportFragmentManager,
                    "AppDrawerBottomSheet"
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViews() {
        context?.let {
            val selectedDrawerLayout = BasePreferenceManager.getString(
                it,
                SharedPrefsConstants.KEY_SELECTED_DRAWER_LAYOUT,
            )
            setUpRecyclerView(selectedDrawerLayout)
        }

        binding.alphabetIndex.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                val y = event.y
                val height = binding.alphabetIndex.height
                val index = (y / height * 26).toInt()
                if (index in 0..25) {
                    val letter = 'A' + index
                    binding.searchEditText.setText(letter.toString())
                    scrollToLetter(letter)
                }
                true
            } else {
                false
            }
        }

        binding.alphabetIndex.text = getAlphabetsList()

    }

    private fun getAlphabetsList(): String {
        var textList = ""
        for (i in 'A'..'Z') {
            textList += "$i\n"
        }
        return textList
    }

    private fun scrollToLetter(letter: Char) {
        adapter?.filter?.filter(letter.toString())
    }

    private fun setUpSearch() {

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter?.filter?.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpRecyclerView(layoutType: String) {
        context?.let {
            lifecycleScope.launch {
                val installedApps = withContext(Dispatchers.IO) { getInstalledAppList(it) }
                installedApps.sortBy { appInfo: AppInfo -> appInfo.label }
                adapter = AppDrawerAdapter(installedApps, layoutType, it, requireActivity().supportFragmentManager)

                binding.appsRv.layoutManager =
                    if (layoutType == AppDrawerLayout.GRID_LAYOUT.toString()) {
                        GridLayoutManager(it, 4)
                    } else {
                        LinearLayoutManager(it)
                    }
                binding.appsRv.adapter = adapter
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInstalledAppList(context: Context): MutableList<AppInfo> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val allApps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

        val installedApps = mutableListOf<AppInfo>()

        for (ri in allApps) {
            val appPackageName = ri.activityInfo.packageName
            val app = AppInfo(
                label = ri.loadLabel(packageManager).toString(),
                packageName = appPackageName,
                icon = ri.activityInfo.loadIcon(packageManager)
            )
            Log.i("AppDrawerFragment", "Social app found: ${app.packageName}")
            installedApps.add(app)
        }
        return installedApps
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLayoutSelected(layoutType: String) {
        setUpRecyclerView(layoutType)
    }

}
