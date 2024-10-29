package com.studynotes.mylauncher.roomDB.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.studynotes.mylauncher.roomDB.Model.RestrictedApp

@Dao
interface RestrictedAppDao {

    @Insert
    suspend fun insertRestrictedApp(restrictedApp: RestrictedApp)

    @Query("DELETE FROM restricted_apps WHERE packageName =  :packageName")
    suspend fun removeFromRestrictedApps(packageName: String)

    @Query("SELECT * FROM restricted_apps")
    fun getAllRestrictedApps() : LiveData<List<RestrictedApp>>

    @Query("SELECT EXISTS(SELECT 1 FROM restricted_apps WHERE packageName = :packageName)")
    suspend fun isAppRestricted(packageName: String): Boolean

}