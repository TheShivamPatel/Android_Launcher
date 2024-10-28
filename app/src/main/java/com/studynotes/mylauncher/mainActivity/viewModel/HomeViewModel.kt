package com.studynotes.mylauncher.mainActivity.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.studynotes.mylauncher.roomDB.Dao.HomeAppDao
import com.studynotes.mylauncher.roomDB.database.HomeAppDatabase

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val database: HomeAppDatabase = HomeAppDatabase.getDatabase(application)
    val homeAppDao: HomeAppDao = database.homeAppDao()
}