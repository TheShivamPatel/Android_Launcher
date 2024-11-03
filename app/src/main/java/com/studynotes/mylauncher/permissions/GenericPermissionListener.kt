package com.studynotes.mylauncher.permissions

interface GenericPermissionListener {
    fun onPermissionGranted()
    fun onPermissionDenied()
    fun onResume()
}