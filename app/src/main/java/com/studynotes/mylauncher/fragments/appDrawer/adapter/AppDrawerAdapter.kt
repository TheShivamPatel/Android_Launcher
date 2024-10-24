package com.studynotes.mylauncher.fragments.appDrawer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.studynotes.mylauncher.databinding.ItemAppIconBinding
import com.studynotes.mylauncher.model.AppInfo

class AppDrawerAdapter(
    private val appList: List<AppInfo>
) : RecyclerView.Adapter<AppDrawerAdapter.IconPackViewHolder>() {

    inner class IconPackViewHolder(val binding: ItemAppIconBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconPackViewHolder {
        val binding = ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconPackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconPackViewHolder, position: Int) {
        val appInfo = appList[position]
        with(holder.binding) {
            tvAppLabel.text = appInfo.label
            imgIcon.setImageDrawable(appInfo.icon)

            root.setOnClickListener {
                val launchIntent = holder.itemView.context.packageManager.getLaunchIntentForPackage(appInfo.packageName!!)

                if (launchIntent != null){
                    holder.itemView.context.startActivity(launchIntent)
                }else{

                }
            }

        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }
}