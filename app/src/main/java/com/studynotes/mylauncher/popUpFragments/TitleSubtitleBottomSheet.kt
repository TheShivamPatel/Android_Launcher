package com.studynotes.mylauncher.popUpFragments

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studynotes.mylauncher.databinding.TitleSubtitlePermissionDialogBinding

class TitleSubtitleBottomSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: TitleSubtitlePermissionDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TitleSubtitlePermissionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClick()
    }

    private fun setOnClick() {
        binding.imgClose.setOnClickListener { dismiss() }
        binding.positiveButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
        }
    }

}