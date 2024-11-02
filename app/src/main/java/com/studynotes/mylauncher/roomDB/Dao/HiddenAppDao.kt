package com.studynotes.mylauncher.roomDB.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.studynotes.mylauncher.roomDB.Model.HiddenApps

@Dao
interface HiddenAppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddictiveApp(hiddenApps: HiddenApps)

    @Query("DELETE FROM addictive_apps WHERE packageName =  :packageName")
    suspend fun removeFromAddictiveApps(packageName: String)

    @Query("SELECT * FROM addictive_apps")
    fun getAllAddictiveApps() : LiveData<List<HiddenApps>>

    @Query("SELECT EXISTS(SELECT 1 FROM addictive_apps WHERE packageName = :packageName)")
    suspend fun isAppAddictive(packageName: String): Boolean

}