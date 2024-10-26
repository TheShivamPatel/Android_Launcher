package com.studynotes.mylauncher.bottomSheet

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.activity.mainHome.MainActivity
import com.studynotes.mylauncher.databinding.BottomSheetSelectTimeLimitBinding
import com.studynotes.mylauncher.model.AppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler


class SelectTimeLimitBottomSheet(val appInfo: AppInfo) : DialogFragment() {

    private lateinit var binding: BottomSheetSelectTimeLimitBinding
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSelectTimeLimitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        binding.imgIcon.setImageDrawable(appInfo.icon)
        binding.tvAppLabel.text = appInfo.label
        setUpTimePicker()
    }

    private fun setUpTimePicker() {
        val intervals = arrayOf("1", "5", "10", "15", "20", "25", "30")
        binding.timePicker.minValue = 1
        binding.timePicker.maxValue = intervals.size - 1
        binding.timePicker.displayedValues = intervals

        binding.tvDone.setOnClickListener {

            val selectedTime = intervals[binding.timePicker.value].toInt() * 1000 // in ms
            Log.d("zzz", "${intervals[binding.timePicker.value].toInt()}")
            startTimer(selectedTime.toLong())
            dismiss()
            launchApp(appInfo)
        }

        binding.tvCancel.setOnClickListener { dismiss() }
    }

    private fun startTimer(duration: Long) {
        job = CoroutineScope(Dispatchers.Main).launch {
            Log.d("zzz", "Timer started for $duration ms")
            android.os.Handler().postDelayed({

                Log.d("zzz", "Timer ended for $duration ms")

            }, duration)
        }
    }

    private fun launchApp(app: AppInfo) {
        val launchIntent = app.packageName?.let {
            binding.root.context.packageManager.getLaunchIntentForPackage(it)
        }
        launchIntent?.let {
            binding.root.context.startActivity(it)
        }
    }


    private fun exitApp() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val rootView = dialog?.findViewById<View>(android.R.id.content)
            rootView?.let {
                val layoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(60, 0, 60, 0)
                it.layoutParams = layoutParams
            }
        }
    }

}