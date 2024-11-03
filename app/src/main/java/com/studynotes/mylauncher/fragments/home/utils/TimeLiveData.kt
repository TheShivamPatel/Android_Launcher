package com.studynotes.mylauncher.fragments.home.utils

import androidx.lifecycle.LiveData
import java.text.SimpleDateFormat
import java.util.*

class TimeLiveData : LiveData<String>() {

    private val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
    private val timer = Timer()

    override fun onActive() {
        super.onActive()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val currentTime = timeFormat.format(Date())
                postValue(currentTime)
            }
        }, 0, 60000) // Update every minute
    }

    override fun onInactive() {
        super.onInactive()
        timer.cancel()
    }
}
