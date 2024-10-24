package com.studynotes.mylauncher.fragments.appDrawer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.studynotes.mylauncher.databinding.IconTitleItemLayoutBinding
import com.studynotes.mylauncher.databinding.ItemAppIconBinding
import com.studynotes.mylauncher.model.AppInfo

class AppDrawerAdapter(
    private val appList: List<AppInfo>,
    private val layoutType: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class LinearIconPackViewHolder(val binding:  IconTitleItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
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
        }
    }


    inner class GridIconPackViewHolder(val binding: ItemAppIconBinding) : RecyclerView.ViewHolder(binding.root) {
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(layoutType == AppDrawerLayout.GRID_LAYOUT.toString()){
            val binding = ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GridIconPackViewHolder(binding)
        }
        else{
            val binding = IconTitleItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LinearIconPackViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val appInfo = appList[position]
        when (holder) {
            is LinearIconPackViewHolder -> holder.bind(appInfo)
            is GridIconPackViewHolder -> holder.bind(appInfo)
        }

    }

    override fun getItemCount(): Int {
        return appList.size
    }
}

enum class AppDrawerLayout {
    LINEAR_LAYOUT, GRID_LAYOUT
}