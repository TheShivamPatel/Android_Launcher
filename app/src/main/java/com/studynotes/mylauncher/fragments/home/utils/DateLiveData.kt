package com.studynotes.mylauncher.fragments.home.utils

import androidx.lifecycle.LiveData
import java.text.SimpleDateFormat
import java.util.*

class DateLiveData : LiveData<String>() {

    private val dateFormat = SimpleDateFormat("EEE, dd MMMM", Locale.getDefault())

    override fun onActive() {
        super.onActive()
        updateDate()
    }

    private fun updateDate() {
        val currentDate = dateFormat.format(Date())
        postValue(currentDate)
    }
}
