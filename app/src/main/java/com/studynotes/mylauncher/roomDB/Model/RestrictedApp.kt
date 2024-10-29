package com.studynotes.mylauncher.roomDB.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restricted_apps")
data class RestrictedApp(@PrimaryKey val packageName: String)