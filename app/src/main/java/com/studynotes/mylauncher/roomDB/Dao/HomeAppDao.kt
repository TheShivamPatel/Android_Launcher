package com.studynotes.mylauncher.roomDB.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.studynotes.mylauncher.roomDB.Model.HomeApp

@Dao
interface HomeAppDao {

    @Insert
    suspend fun insertHomeApp(homeApp : HomeApp)

    @Delete
    suspend fun deleteHomeApp(homeApp: HomeApp)

    @Query("SELECT * FROM home_apps")
    fun getAllHomeApps(): LiveData<List<HomeApp>>

}