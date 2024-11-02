package com.studynotes.mylauncher.popUpFragments.themeColorPicker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.ItemColorThemeBinding

class ColorThemeAdapter(private val context: Context, private val colorList: List<Int>, private val onSelect: (Int) -> Unit) :
    RecyclerView.Adapter<ColorThemeAdapter.ColorThemeViewHolder>() {

    inner class ColorThemeViewHolder(val binding: ItemColorThemeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorThemeViewHolder {
        val binding =
            ItemColorThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorThemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorThemeViewHolder, position: Int) {
        val colorTheme = colorList[position]
        with(holder.binding) {
            root.setOnClickListener { onSelect(colorTheme) }
            root.setBackgroundColor(colorTheme)
        }
    }

    override fun getItemCount(): Int = colorList.size

}