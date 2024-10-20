package com.studynotes.mylauncher.activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.ActivityMainBinding
import com.studynotes.mylauncher.fragments.home.HomeScreenFragment
import com.studynotes.mylauncher.viewUtils.ViewUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewUtils.setTransparentNavigationBar(this)
        setUpViews()
        setUpStatusBar()
    }

    private fun setUpStatusBar() {
        ViewUtils.setUpStatusBar(this, false)
        ViewUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.transparent))
    }

    private fun setUpViews() {
        setUpFragment(HomeScreenFragment())
    }

    private fun setUpFragment(fragment: Fragment?) {
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.container.id, it)
                .commit()
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.container.id)

        if (currentFragment is HomeScreenFragment) {
            super.onBackPressed()
        } else {
            setUpFragment(HomeScreenFragment())
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("zzz", "Action was DOWN")
                true
            }
            MotionEvent.ACTION_UP -> {
                Log.d("zzz", "Action was UP")
                true
            }
            else -> super.onTouchEvent(event)
        }
    }


}
