package com.studynotes.mylauncher.roomDB.Model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "home_apps")
data class HomeApp(
    val label: String?,
    @PrimaryKey val packageName: String,
    val iconData: ByteArray?
)