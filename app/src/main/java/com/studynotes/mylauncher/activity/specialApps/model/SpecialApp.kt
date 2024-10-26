package com.studynotes.mylauncher.activity.specialApps.model

import android.graphics.drawable.Drawable

data class SpecialApp(
    val icon: Drawable?,
    val label: String?,
    val packageName: String?,
    var isEnabled: Boolean?
)

