package com.studynotes.mylauncher.roomDB.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.studynotes.mylauncher.roomDB.Model.HomeApp

@Dao
interface HomeAppDao {

    @Insert
    suspend fun insertHomeApp(homeApp : HomeApp)

    @Query("DELETE FROM home_apps WHERE packageName = :packageName")
    suspend fun deleteHomeAppByPackageName(packageName: String)

    @Query("SELECT * FROM home_apps")
    fun getAllHomeApps(): LiveData<List<HomeApp>>

    @Query("SELECT EXISTS(SELECT 1 FROM home_apps WHERE packageName = :packageName)")
    suspend fun isAppInHome(packageName: String): Boolean

    @Query("SELECT COUNT(*) FROM home_apps")
    suspend fun getHomeAppCount() : Int
}