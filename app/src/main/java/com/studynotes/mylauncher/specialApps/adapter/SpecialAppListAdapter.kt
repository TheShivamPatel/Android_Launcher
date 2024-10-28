package com.studynotes.mylauncher.specialApps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.studynotes.mylauncher.specialApps.model.SpecialApp
import com.studynotes.mylauncher.databinding.IconSwitchSettingItemBinding


class SpecialAppAdapter(private val specialAppList: List<SpecialApp>) :
    RecyclerView.Adapter<SpecialAppAdapter.SpecialAppViewHolder>() {

    inner class SpecialAppViewHolder(val binding: IconSwitchSettingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialAppViewHolder {
        val binding =
            IconSwitchSettingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialAppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecialAppViewHolder, position: Int) {
        val app = specialAppList[position]

        with(holder.binding) {
            title.text = app.label
            leadingIcon.setImageDrawable(app.icon)

        }

    }

    override fun getItemCount(): Int = specialAppList.size

}
