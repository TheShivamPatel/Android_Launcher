package com.studynotes.mylauncher.roomDB.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addictive_apps")
data class HiddenApps(@PrimaryKey val packageName: String)
