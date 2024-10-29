package com.studynotes.mylauncher.roomDB.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.studynotes.mylauncher.roomDB.Dao.HomeAppDao
import com.studynotes.mylauncher.roomDB.Dao.RestrictedAppDao
import com.studynotes.mylauncher.roomDB.Model.HomeApp
import com.studynotes.mylauncher.roomDB.Model.RestrictedApp
import com.studynotes.mylauncher.roomDB.convertors.Convertors

@Database(entities = [HomeApp::class, RestrictedApp::class], version = 1)
@TypeConverters(Convertors::class)
abstract class LauncherDatabase : RoomDatabase() {

    abstract fun homeAppDao(): HomeAppDao
    abstract fun restrictedAppsDao() : RestrictedAppDao

    companion object {
        @Volatile
        private var INSTANCE: LauncherDatabase? = null

        fun getDatabase(context: Context): LauncherDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LauncherDatabase::class.java,
                        "LauncherDB"
                    ).build()
                }
            }
            return INSTANCE as LauncherDatabase
        }
    }
}