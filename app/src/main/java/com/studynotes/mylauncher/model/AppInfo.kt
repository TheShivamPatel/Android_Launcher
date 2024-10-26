package com.studynotes.mylauncher.model
import android.graphics.drawable.Drawable
import java.io.Serializable

data class AppInfo(
    val label: String?,
    val packageName: String?,
    val icon: Drawable?
) : Serializable
