package com.studynotes.mylauncher.popUpFragments.themeColorPicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studynotes.mylauncher.databinding.BottomSheetThemeColorPickerBinding
import com.studynotes.mylauncher.popUpFragments.themeColorPicker.adapter.ColorThemeAdapter

class ThemeColorPickerBottomSheet(val selectedTheme: (Int) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetThemeColorPickerBinding
    private var adapter: ColorThemeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetThemeColorPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        context?.let {
            adapter = ColorThemeAdapter(it, getAllListTheme()) { color ->
                selectedTheme(color)
            }
            binding.rvTheme.layoutManager = GridLayoutManager(it, 4)
            binding.rvTheme.adapter = adapter
        }
    }

    private fun getAllListTheme(): List<Int> {
        val colors = listOf(
            0xFF0B0B0B.toInt(),
            0xFF29375A.toInt(),
            0xFF7A0000.toInt(),
            0xFF393D46.toInt(),
            0xFF693E52.toInt(),
            0xFF1B1A55.toInt(),
            0xFF070F2B.toInt(),
            0xFF344955.toInt(),
            0xFF1B4242.toInt(),
            0xFF1F6E8C.toInt(),
            0xFF27374D.toInt(),
        )
        return colors
    }

}