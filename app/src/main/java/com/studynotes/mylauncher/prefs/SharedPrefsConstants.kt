package com.studynotes.mylauncher.prefs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPrefsConstants {

    companion object {
        const val PREFS_FILE_NAME = "next_level_ai_resume_builder"

        fun isPathExists(context: Context): Boolean {
            val sh = context.getSharedPreferences(Constants.DATA_PATH, AppCompatActivity.MODE_PRIVATE)
            val uriPath = sh.getString(Constants.PATH, "")
            if (uriPath != null) {
                if (uriPath != Constants.ANDROID_DIR_DEFAULT_PATH + Constants.TARGET_DIR_PATH_ANDROID_Q_OR_GREATER) {
                    return false
                }
                if (uriPath.isEmpty()) {
                    return false
                }
            }
            return true
        }

        fun getUriPath(context: Context): String? {
            val sh =
                context.getSharedPreferences(Constants.DATA_PATH, AppCompatActivity.MODE_PRIVATE)
            return sh.getString(Constants.PATH, "")
        }

        fun saveUriPath(context: Context, path: String) {
            val sharedPreferences =
                context.getSharedPreferences(Constants.DATA_PATH, Context.MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()
            myEdit.putString(Constants.PATH, path)
            myEdit.apply()
        }

    }
}


object Constants {
    const val DATA_PATH = "data_path"
    const val PATH = "path"
    const val REQUEST_CODE_FOR_STORAGE_PERMISSION_FOR_Q_AND_ABOVE = 121
    const val REQUEST_CODE_FOR_STORAGE_PERMISSION_FOR_BELOW_Q = 122
    const val ANDROID_DIR_DEFAULT_PATH = "content://com.android.externalstorage.documents/tree/primary%3A"
    const val TARGET_DIR_PATH_ANDROID_Q_OR_GREATER = "Documents"
    const val SAVE_FOLDER_NAME = "NextLeve_AI_Resume"
}