package com.studynotes.mylauncher.fragments.appDrawer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.studynotes.mylauncher.bottomSheet.AppSettingsBottomSheet
import com.studynotes.mylauncher.bottomSheet.SelectAppDrawerLayoutBottomSheet
import com.studynotes.mylauncher.bottomSheet.SelectTimeLimitDialog
import com.studynotes.mylauncher.databinding.IconTitleItemLayoutBinding
import com.studynotes.mylauncher.databinding.ItemAppIconBinding
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.viewUtils.ViewUtils

class AppDrawerAdapter(
    private val appList: List<AppInfo>,
    private val layoutType: String,
    private val context: Context,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable,
    SelectAppDrawerLayoutBottomSheet.OnLayoutSelectedListener {

    private var appListFiltered: List<AppInfo> = appList

    inner class LinearIconPackViewHolder(val binding: IconTitleItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(appInfo: AppInfo) {
            binding.tvAppLabel.text = appInfo.label
            binding.imgIcon.setImageDrawable(appInfo.icon)
            binding.root.setOnClickListener {
                SelectTimeLimitDialog(
                    SelectTimeLimitDialog.Companion.DialogType.TYPE_SELECT_TIME_LIMIT,
                    appInfo
                ).show(fragmentManager, SelectTimeLimitDialog.TAG)
            }

            binding.root.setOnLongClickListener {
                AppSettingsBottomSheet(appInfo).show(
                    fragmentManager,
                    "AppDrawerBottomSheet"
                )
                return@setOnLongClickListener true
            }

        }
    }


    inner class GridIconPackViewHolder(val binding: ItemAppIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(appInfo: AppInfo) {
            binding.tvAppLabel.text = appInfo.label
            binding.imgIcon.setImageDrawable(appInfo.icon)
            binding.root.setOnClickListener {
                val launchIntent = appInfo.packageName?.let { it1 ->
                    binding.root.context.packageManager.getLaunchIntentForPackage(
                        it1
                    )
                }
                launchIntent?.let { binding.root.context.startActivity(it) }
            }

            binding.root.setOnLongClickListener {
                AppSettingsBottomSheet(appInfo).show(
                    fragmentManager,
                    "AppDrawerBottomSheet"
                )
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (layoutType == AppDrawerLayout.GRID_LAYOUT.toString()) {
            val binding =
                ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GridIconPackViewHolder(binding)
        } else {
            val binding = IconTitleItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            LinearIconPackViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val appInfo = appListFiltered[position]
        when (holder) {
            is LinearIconPackViewHolder -> holder.bind(appInfo)
            is GridIconPackViewHolder -> holder.bind(appInfo)
        }

    }

    override fun getItemCount(): Int {
        return appListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""

                val filteredList = if (query.isEmpty()) {
                    appList
                } else {
                    appList.filter {
                        it.label?.lowercase()?.startsWith(query) == true
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults

            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                appListFiltered = results?.values as List<AppInfo>
                notifyDataSetChanged()
            }

        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isHeader(position: Int): Boolean {
        if (position == 0) return true
        val currentLabel = appListFiltered[position].label?.get(0)
        val previousLabel = appListFiltered[position - 1].label?.get(0)
        return currentLabel != previousLabel
    }

    override fun onLayoutSelected(layoutType: String) {
        ViewUtils.showToast(context = context, "$layoutType Selected")
    }


}

enum class AppDrawerLayout {
    LINEAR_LAYOUT, GRID_LAYOUT
}