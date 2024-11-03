package com.studynotes.mylauncher.permissions

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

fun isOverlayPermissionGranted(context: Context): Boolean {
    return Settings.canDrawOverlays(context)
}

fun isAppUsagePermissionGranted(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val appOpsManager = ContextCompat.getSystemService(context, AppOpsManager::class.java)
        val mode = appOpsManager?.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    return false
}
