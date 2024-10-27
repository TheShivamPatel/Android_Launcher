package com.studynotes.mylauncher.bottomSheet

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.studynotes.mylauncher.databinding.DialogSelectTimeLimitBinding
import com.studynotes.mylauncher.model.AppInfo
import com.studynotes.mylauncher.services.SpendTimeLimitService

class SelectTimeLimitDialog(private val dialogType: DialogType, private val appInfo: AppInfo) : DialogFragment() {

    private lateinit var binding: DialogSelectTimeLimitBinding
    private val intervals = arrayOf("1", "5", "10", "15", "20", "25", "30")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSelectTimeLimitBinding.inflate(inflater, container, false)
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

        when(dialogType.toString()){

            DialogType.TYPE_SELECT_TIME_LIMIT.toString() -> {
                binding.timePicker.visibility = View.VISIBLE
                binding.tvDone.setOnClickListener {
                    val selectedTime = intervals[binding.timePicker.value].toInt() * 1000 // in ms
                    startBackgroundService(selectedTime.toLong())
                    dismiss()
                    launchApp(appInfo)
                }
                binding.tvCancel.setOnClickListener { dismiss() }
            }

            DialogType.TYPE_EXTEND_TIME_LIMIT.toString() -> {
                binding.tvDone.text = "Close"
                binding.tvCancel.text = "Extend"
                binding.tvDone.setOnClickListener { dismiss() }
                binding.tvCancel.setOnClickListener { setUpTimePicker() }
            }

        }
    }

    private fun setUpTimePicker() {
        binding.timePicker.minValue = 0
        binding.timePicker.maxValue = intervals.size -1
        binding.timePicker.displayedValues = intervals
    }

    private fun startBackgroundService(duration: Long) {
        val serviceIntent = Intent(context, SpendTimeLimitService::class.java)
        serviceIntent.putExtra("EXTRA_DURATION", duration)
        context?.startService(serviceIntent)
    }

    private fun launchApp(app: AppInfo) {
        val launchIntent = app.packageName?.let {
            binding.root.context.packageManager.getLaunchIntentForPackage(it)
        }
        launchIntent?.let {
            binding.root.context.startActivity(it)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            isCancelable = false
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


    companion object {
        const val TAG = "SelectTimeLimitDialog"

        enum class DialogType {
            TYPE_SELECT_TIME_LIMIT, TYPE_EXTEND_TIME_LIMIT
        }

    }

}