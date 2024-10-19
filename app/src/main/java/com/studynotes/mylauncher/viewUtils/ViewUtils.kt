package com.techprojects.nextlevel_airesumebuilder.viewUtils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.techprojects.nextlevel_airesumebuilder.viewUtils.ViewUtils.hideKeyboard
import kotlin.math.roundToInt


object ViewUtils {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setUpStatusBar(activity: Activity, isDark: Boolean) {
        if (isDark) {
            removeLightStatusBar(activity)
        } else {
            addLightStatusBar(activity)
        }
    }

    fun removeLightStatusBar(activity: Activity) {
        val view = activity.window.decorView
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun addLightStatusBar(activity: Activity) {
        val view = activity.window.decorView
        var flags = view.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        view.systemUiVisibility = flags
    }

    fun setStatusBarColor(activity: Activity, color: Int) {
        try {
            activity.window?.statusBarColor = color
        } catch (e: Exception) {
            Log.e("TAG", "setting status bar color error")
        }
    }

    fun View.hideKeyboard(context: Context) {
        try {
            (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
                this.rootView.windowToken,
                0
            )
        } catch (e: Exception) {
        }
    }

    fun View.showKeyboard(context: Context) {
        try {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                this, InputMethodManager.SHOW_IMPLICIT
            )
        } catch (e: Exception) {
        }
    }


    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }

    fun setUpTransparentStatusBar(activity: Activity) {
        try {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        } catch (e: Exception) {

        }
    }

    fun setRoundedRectangleBackgroundDrawable(
        view: View, backgroundColor: Int, cornerRadius: Float
    ) {
        view.background = getRoundedRectangleBackgroundDrawable(view, backgroundColor, cornerRadius)
    }

    fun getRoundedRectangleBackgroundDrawable(
        view: View,
        backgroundColor: Int,
        cornerRadius: Float
    ): GradientDrawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(backgroundColor)
        view.background = shape
        if (cornerRadius > 0) {
            shape.cornerRadius = cornerRadius
        }
        return shape
    }

}