package com.studynotes.mylauncher.services

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.activity.mainHome.MainActivity
import com.studynotes.mylauncher.databinding.DialogSelectTimeLimitBinding
import com.studynotes.mylauncher.viewUtils.ViewUtils

class SpendTimeLimitService : Service() {

    private var overlayView: View? = null
    private lateinit var windowManager: WindowManager
    private lateinit var binding: DialogSelectTimeLimitBinding
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler

    override fun onCreate() {
        super.onCreate()
        handlerThread = HandlerThread("OverlayHandlerThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("zzz", "Service Started")
        val duration = intent?.getLongExtra("EXTRA_DURATION", 5000L) ?: 5000L
        performLongTask(duration)
        return START_STICKY
    }

    private fun performLongTask(duration: Long) {
        handler.postDelayed({
            Log.d("zzz", "Time's UP! From Broadcast")
            showOverlayDialog()
        }, duration)
    }

    private fun showOverlayDialog() {

        if (Settings.canDrawOverlays(this)) {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            overlayView = inflater.inflate(R.layout.dialog_select_time_limit, null)
            binding = DialogSelectTimeLimitBinding.bind(overlayView!!)

            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
            )
            layoutParams.gravity = Gravity.CENTER
            layoutParams.x = 60
            layoutParams.y = 60

            windowManager.addView(overlayView, layoutParams)

            setupOverlayActions()
        }

    }

    private fun setupOverlayActions() {
        binding.tvDone.text = "Close"
        binding.tvCancel.text = "Extend"
        binding.limitDesc.text = "Limit reached! It’s time to step back…\nunless you really need another minute."
        binding.timePicker.visibility = View.GONE
        binding.spacer.visibility = View.VISIBLE

        binding.tvDone.setOnClickListener {
            windowManager.removeView(overlayView)
            overlayView = null
            stopSelf()
            exitApp()
        }

        binding.tvCancel.setOnClickListener {
            windowManager.removeView(overlayView)
            ViewUtils.showToast(this, "One more minute! Use it wisely to stay on track.")
            performLongTask(10000L)
        }
    }

//    private fun exitApp() {
//        val intent = Intent(Intent.ACTION_MAIN)
//        intent.addCategory(Intent.CATEGORY_HOME)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//    }

    private fun exitApp() {
        val intent = Intent(this, MainActivity::class.java) // Replace with your launcher activity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        overlayView?.let { windowManager.removeView(it) }
        handlerThread.quitSafely()

    }

}