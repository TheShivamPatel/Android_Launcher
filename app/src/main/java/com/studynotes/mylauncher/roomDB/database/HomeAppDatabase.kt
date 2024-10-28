package com.studynotes.mylauncher.roomDB.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.studynotes.mylauncher.roomDB.Dao.HomeAppDao
import com.studynotes.mylauncher.roomDB.Model.HomeApp
import com.studynotes.mylauncher.roomDB.convertors.Convertors

@Database(entities = [HomeApp::class], version = 1)
@TypeConverters(Convertors::class)
abstract class HomeAppDatabase : RoomDatabase() {

    abstract fun homeAppDao(): HomeAppDao

    companion object {

        @Volatile
        private var INSTANCE: HomeAppDatabase? = null

        fun getDatabase(context: Context): HomeAppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        HomeAppDatabase::class.java,
                        "homeAppDB"
                    ).build()
                }
            }
            return INSTANCE as HomeAppDatabase
        }
    }
}