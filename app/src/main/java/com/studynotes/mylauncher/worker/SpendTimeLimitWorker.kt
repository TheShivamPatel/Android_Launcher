package com.studynotes.mylauncher.worker

import android.app.Service.LAYOUT_INFLATER_SERVICE
import android.app.Service.WINDOW_SERVICE
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.DialogSelectTimeLimitBinding
import com.studynotes.mylauncher.fragments.appDrawer.model.AppInfo
import com.studynotes.mylauncher.mainActivity.MainActivity
import com.studynotes.mylauncher.viewUtils.ViewUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SpendTimeLimitWorker(val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private var overlayView: View? = null
    private lateinit var windowManager: WindowManager
    private lateinit var binding: DialogSelectTimeLimitBinding
    private var packageName : String? = null

    override suspend fun doWork(): Result {
        packageName = inputData.getString("WORK_PACKAGE_NAME") ?: return Result.failure()

        withContext(Dispatchers.Main) {
            showOverlayDialog()
        }

        return Result.success()
    }

    // STUCK IN THIS
    fun isAppInForeground(context: Context, packageName: String): Boolean {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        Log.d("UsageStats", "Usage Stats: $usageStatsManager")

        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 5000

        val events = usageStatsManager.queryEvents(beginTime, endTime)
        var isInForeground = false

        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)

            if (event.packageName == packageName) {
                if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    isInForeground = true
                    break
                }
            }
        }

        return isInForeground
    }


    private fun showOverlayDialog() {

        if (Settings.canDrawOverlays(context.applicationContext)) {
            windowManager =
                context.applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
            val inflater =
                context.applicationContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            overlayView = inflater.inflate(R.layout.dialog_select_time_limit, null)
            binding = DialogSelectTimeLimitBinding.bind(overlayView!!)

            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
            )
            layoutParams.gravity = Gravity.CENTER

            windowManager.addView(overlayView, layoutParams)
            setUpOverlayView()
            setupOverlayActions()
        }
    }

    private fun setUpOverlayView() {
        binding.tvDone.text = "Close"
        binding.tvCancel.text = "Extend"
        binding.limitDesc.text =
            "Limit reached! It’s time to step back…\nunless you really need another minute."
        binding.timePicker.visibility = View.GONE
        binding.llAppInfo.visibility = View.GONE
        binding.spacer.visibility = View.VISIBLE
    }

    private fun setupOverlayActions() {

        binding.tvDone.setOnClickListener {
            windowManager.removeView(overlayView)
            overlayView = null
            exitApp()
        }

        binding.tvCancel.setOnClickListener {
            windowManager.removeView(overlayView)
            CoroutineScope(Dispatchers.Main).launch {
                ViewUtils.showToast(context.applicationContext, "One more minute! Use it wisely to stay on track.")
            }
            val inputData = workDataOf("WORK_PACKAGE_NAME" to packageName)
            val spendTimeLimitWorker: WorkRequest =
                OneTimeWorkRequestBuilder<SpendTimeLimitWorker>()
                    .setInitialDelay(30, TimeUnit.SECONDS)
                    .setInputData(inputData)
                    .build()
            WorkManager.getInstance(context).enqueue(spendTimeLimitWorker)
        }
    }

    private fun exitApp() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

//    private fun exitApp() {
//        val intent = Intent(context, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        context.startActivity(intent)
//    }
}